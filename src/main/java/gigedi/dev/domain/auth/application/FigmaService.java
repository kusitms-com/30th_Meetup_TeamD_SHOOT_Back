package gigedi.dev.domain.auth.application;

import static gigedi.dev.global.common.constants.SecurityConstants.FIGMA_GET_ID_TOKEN_URL;
import static gigedi.dev.global.common.constants.SecurityConstants.FIGMA_GET_USER_INFO_URL;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import gigedi.dev.domain.auth.dto.response.FigmaTokenResponse;
import gigedi.dev.domain.auth.dto.response.FigmaUserResponse;
import gigedi.dev.domain.auth.dto.response.UserInfoResponse;
import gigedi.dev.global.error.exception.CustomException;
import gigedi.dev.global.error.exception.ErrorCode;
import gigedi.dev.infra.config.oauth.FigmaProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class FigmaService {

    private final FigmaProperties figmaProperties;
    private final RestClient restClient;

    public String getAccessToken(String code) {
        try {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("client_id", figmaProperties.id());
            formData.add("client_secret", figmaProperties.secret());
            formData.add("redirect_uri", figmaProperties.redirectUri());
            formData.add("code", code);
            formData.add("grant_type", "authorization_code");

            String responseBody =
                    restClient
                            .post()
                            .uri(FIGMA_GET_ID_TOKEN_URL)
                            .header(
                                    HttpHeaders.CONTENT_TYPE,
                                    MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                            .body(formData)
                            .retrieve()
                            .onStatus(
                                    status -> !status.is2xxSuccessful(),
                                    (request, responseEntity) -> {
                                        log.error(
                                                "Figma ID Token 요청 실패: {}",
                                                responseEntity.getStatusCode());
                                        throw new CustomException(ErrorCode.FIGMA_LOGIN_FAILED);
                                    })
                            .body(String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            FigmaTokenResponse figmaTokenResponse =
                    objectMapper.readValue(responseBody, FigmaTokenResponse.class);

            return figmaTokenResponse.accessToken();

        } catch (Exception e) {
            log.error("Figma 로그인 중 예외 발생 : {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.FIGMA_LOGIN_FAILED);
        }
    }

    public UserInfoResponse getUserInfo(String accessToken) {
        String userInfoUrl = FIGMA_GET_USER_INFO_URL;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        try {
            FigmaUserResponse figmaUserResponse =
                    restClient
                            .get()
                            .uri(userInfoUrl)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                            .retrieve()
                            .onStatus(
                                    status -> !status.is2xxSuccessful(),
                                    (request, response) -> {
                                        log.error(
                                                "Figma 유저 정보 요청 실패: {}", response.getStatusCode());
                                        throw new CustomException(ErrorCode.FIGMA_USER_INFO_FAILED);
                                    })
                            .body(FigmaUserResponse.class);

            if (figmaUserResponse == null) {
                throw new CustomException(ErrorCode.FIGMA_USER_INFO_NOT_FOUND);
            }

            return new UserInfoResponse(
                    figmaUserResponse.handle(),
                    figmaUserResponse.email(),
                    figmaUserResponse.img_url(),
                    figmaUserResponse.id());

        } catch (Exception e) {
            log.error("Figma 유저 정보 조회 중 예외 발생 : " + e.getMessage(), e);
            throw new CustomException(ErrorCode.FIGMA_USER_INFO_FAILED);
        }
    }
}
