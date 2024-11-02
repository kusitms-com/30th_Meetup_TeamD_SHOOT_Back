package gigedi.dev.domain.auth.application;

import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gigedi.dev.domain.auth.dto.AccessTokenDto;
import gigedi.dev.domain.auth.dto.RefreshTokenDto;
import gigedi.dev.domain.auth.dto.request.TokenRefreshRequest;
import gigedi.dev.domain.auth.dto.response.GoogleLoginResponse;
import gigedi.dev.domain.auth.dto.response.TokenPairResponse;
import gigedi.dev.domain.member.dao.MemberRepository;
import gigedi.dev.domain.member.domain.Member;
import gigedi.dev.domain.member.domain.OauthInfo;
import gigedi.dev.global.error.exception.CustomException;
import gigedi.dev.global.error.exception.ErrorCode;
import gigedi.dev.global.util.MemberUtil;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final GoogleService googleService;
    private final IdTokenVerifier idTokenVerifier;
    private final MemberRepository memberRepository;
    private final JwtTokenService jwtTokenService;
    private final MemberUtil memberUtil;

    public TokenPairResponse googleSocialLogin(String code) {
        OidcUser user = getOidcUserFromGoogle(code);
        Member member = getOrCreateMember(user);
        return createTokenPair(member);
    }

    public TokenPairResponse refreshToken(TokenRefreshRequest request) {
        RefreshTokenDto oldRefreshTokenDto =
                jwtTokenService.validateRefreshToken(request.getRefreshToken());

        if (oldRefreshTokenDto == null) {
            throw new CustomException(ErrorCode.EXPIRED_JWT_TOKEN);
        }
        RefreshTokenDto newRefreshTokenDto =
                jwtTokenService.refreshRefreshToken(oldRefreshTokenDto);
        AccessTokenDto accessTokenDto =
                jwtTokenService.refreshAccessToken(getMember(newRefreshTokenDto));
        return new TokenPairResponse(accessTokenDto.getToken(), newRefreshTokenDto.getToken());
    }

    public void memberLogout() {
        final Member currentMember = memberUtil.getCurrentMember();
        jwtTokenService.deleteRefreshToken(currentMember);
    }

    private Member getMember(RefreshTokenDto refreshTokenDto) {
        return memberRepository
                .findById(refreshTokenDto.getMemberId())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    private OidcUser getOidcUserFromGoogle(String code) {
        GoogleLoginResponse response = googleService.getIdTokenByGoogleLogin(code);
        return idTokenVerifier.getOidcUser(response.getIdToken());
    }

    private Member getOrCreateMember(OidcUser user) {
        OauthInfo oauthInfo = createOauthInfo(user);
        return memberRepository
                .findByOauthInfoGoogleId(oauthInfo.getGoogleId())
                .filter(member -> member.getDeletedAt() == null)
                .orElseGet(() -> memberRepository.save(Member.createMember(oauthInfo)));
    }

    private OauthInfo createOauthInfo(OidcUser user) {
        Object picture = user.getAttribute("picture");
        String profileImg = picture != null ? picture.toString() : null;

        return OauthInfo.of(user.getSubject(), user.getEmail(), user.getClaim("name"), profileImg);
    }

    private TokenPairResponse createTokenPair(Member member) {
        String accessToken = jwtTokenService.createAccessToken(member.getId(), member.getRole());
        String refreshToken = jwtTokenService.createRefreshToken(member.getId());
        return new TokenPairResponse(accessToken, refreshToken);
    }
}
