package gigedi.dev.domain.auth.application;

import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gigedi.dev.domain.auth.dto.response.GoogleLoginResponse;
import gigedi.dev.domain.auth.dto.response.TokenPairResponse;
import gigedi.dev.domain.member.dao.MemberRepository;
import gigedi.dev.domain.member.domain.Member;
import gigedi.dev.domain.member.domain.OauthInfo;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final GoogleService googleService;
    private final IdTokenVerifier idTokenVerifier;
    private final MemberRepository memberRepository;
    private final JwtTokenService jwtTokenService;

    public TokenPairResponse googleSocialLogin(String code) {
        OidcUser user = getOidcUserFromGoogle(code);
        Member member = getOrCreateMember(user);
        return createTokenPair(member);
    }

    private OidcUser getOidcUserFromGoogle(String code) {
        GoogleLoginResponse response = googleService.getIdTokenByGoogleLogin(code);
        return idTokenVerifier.getOidcUser(response.getIdToken());
    }

    private Member getOrCreateMember(OidcUser user) {
        OauthInfo oauthInfo = createOauthInfo(user);
        return memberRepository
                .findByOauthInfo(oauthInfo)
                .orElseGet(() -> memberRepository.save(Member.createMember(oauthInfo)));
    }

    private OauthInfo createOauthInfo(OidcUser user) {
        return OauthInfo.of(
                user.getSubject(), user.getClaim("name"), user.getAttribute("picture").toString());
    }

    private TokenPairResponse createTokenPair(Member member) {
        String accessToken =
                jwtTokenService.createAccessToken(member.getId(), member.getMemberRole());
        String refreshToken = jwtTokenService.createRefreshToken(member.getId());
        return new TokenPairResponse(accessToken, refreshToken);
    }
}
