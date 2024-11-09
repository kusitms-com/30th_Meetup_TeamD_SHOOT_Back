package gigedi.dev.domain.auth.application;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import gigedi.dev.domain.auth.dao.FigmaRepository;
import gigedi.dev.domain.auth.domain.Figma;
import gigedi.dev.domain.auth.dto.response.FigmaLoginResponse;
import gigedi.dev.global.error.exception.CustomException;
import gigedi.dev.global.error.exception.ErrorCode;
import gigedi.dev.infra.config.oauth.FigmaProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FigmaService {
    private final FigmaProperties figmaProperties;
    private final FigmaRepository figmaRepository;
    private final RestClient restClient;

    public FigmaLoginResponse getAccessTokenByFigmaLogin(String code) {
        try {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("client_id", figmaProperties.getClientId());
            formData.add("client_secret", figmaProperties.getClientSecret());
            formData.add("redirect_uri", figmaProperties.getRedirectUri());
            formData.add("code", code);
            formData.add("grant_type", "authorization_code");

            return restClient
                    .post()
                    .uri(figmaProperties.getTokenUrl())
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                    .body(formData)
                    .retrieve()
                    .onStatus(
                            status -> !status.is2xxSuccessful(),
                            (request, response) -> {
                                log.error("Figma OAuth 요청 실패: {}", response.getStatusCode());
                                throw new CustomException(ErrorCode.FIGMA_LOGIN_FAILED);
                            })
                    .body(FigmaLoginResponse.class);
        } catch (Exception e) {
            log.error("Figma 로그인 중 예외 발생 : {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.FIGMA_LOGIN_FAILED);
        }
    }

    public void saveFigmaToken(Long memberId, String accessToken) {
        figmaRepository.save(Figma.of(memberId, accessToken));
    }
}
