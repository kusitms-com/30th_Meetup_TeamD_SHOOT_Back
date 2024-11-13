package gigedi.dev.domain.auth.application;

import static gigedi.dev.global.common.constants.SecurityConstants.FIGMA_GET_ID_TOKEN_URL;
import static gigedi.dev.global.common.constants.SecurityConstants.FIGMA_GET_USER_INFO_URL;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import gigedi.dev.domain.auth.dto.response.FigmaTokenResponse;
import gigedi.dev.domain.auth.dto.response.FigmaUserResponse;
import gigedi.dev.domain.auth.dto.response.UserInfoResponse;
import gigedi.dev.global.error.exception.CustomException;
import gigedi.dev.global.error.exception.ErrorCode;
import gigedi.dev.infra.config.oauth.FigmaProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class FigmaService {

    private final FigmaProperties figmaProperties;
    private final RestTemplate restTemplate;

    public String getAccessToken(String code) {
        try {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("client_id", figmaProperties.getId());
            formData.add("client_secret", figmaProperties.getSecret());
            formData.add("redirect_uri", figmaProperties.getRedirectUri());
            formData.add("code", code);
            formData.add("grant_type", "authorization_code");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            ResponseEntity<String> response =
                    restTemplate.exchange(
                            FIGMA_GET_ID_TOKEN_URL,
                            HttpMethod.POST,
                            new org.springframework.http.HttpEntity<>(formData, headers),
                            String.class);

            String responseBody = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            FigmaTokenResponse figmaTokenResponse =
                    objectMapper.readValue(responseBody, FigmaTokenResponse.class);
            return figmaTokenResponse.getAccessToken();

        } catch (Exception e) {
            log.error("Figma 로그인 중 예외 발생 : " + e.getMessage(), e);
            throw new CustomException(ErrorCode.FIGMA_LOGIN_FAILED);
        }
    }

    public UserInfoResponse getUserInfo(String accessToken) {
        String userInfoUrl = FIGMA_GET_USER_INFO_URL;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<FigmaUserResponse> response =
                    restTemplate.exchange(
                            userInfoUrl, HttpMethod.GET, entity, FigmaUserResponse.class);
            FigmaUserResponse figmaUserResponse = response.getBody();
            if (figmaUserResponse == null) {
                throw new CustomException(ErrorCode.FIGMA_USER_INFO_NOT_FOUND);
            }

            return new UserInfoResponse(
                    figmaUserResponse.getHandle(),
                    figmaUserResponse.getEmail(),
                    figmaUserResponse.getImg_url(),
                    figmaUserResponse.getId());
        } catch (Exception e) {
            log.error("Figma 유저 정보 조회 중 예외 발생 : " + e.getMessage(), e);
            throw new CustomException(ErrorCode.FIGMA_USER_INFO_FAILED);
        }
    }
}
