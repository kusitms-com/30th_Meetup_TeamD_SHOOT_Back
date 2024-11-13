package gigedi.dev.domain.auth.application;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import gigedi.dev.domain.auth.dto.response.FigmaTokenResponse;
import gigedi.dev.domain.auth.dto.response.FigmaUserResponse;
import gigedi.dev.domain.auth.dto.response.UserInfoResponse;

@Service
public class FigmaService {

    @Value("${figma.client.id}")
    private String clientId;

    @Value("${figma.client.secret}")
    private String clientSecret;

    @Value("${figma.redirect.uri}")
    private String redirectUri;

    private final RestTemplate restTemplate = new RestTemplate();
    private final WebClient webClient = WebClient.create();

    public String getAccessToken(String code) {
        String responseBody =
                webClient
                        .post()
                        .uri("https://www.figma.com/api/oauth/token")
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .bodyValue(
                                "client_id="
                                        + "OSEOcfCVk52Uci4uNnFRb9"
                                        + "&client_secret="
                                        + "zCW4KZz6VnxAnqDdfrs7OdiMvzUHKo"
                                        + "&redirect_uri="
                                        + "http://localhost:3000"
                                        + "&code="
                                        + code
                                        + "&grant_type=authorization_code")
                        .retrieve()
                        .bodyToMono(String.class)
                        .timeout(Duration.ofSeconds(10))
                        .doOnError(
                                error ->
                                        System.out.println(
                                                "Error retrieving access token: "
                                                        + error.getMessage()))
                        .block();

        System.out.println("Response Body: " + responseBody); // 응답 내용 확인

        try {
            // 응답 JSON을 FigmaTokenResponse로 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            FigmaTokenResponse figmaTokenResponse =
                    objectMapper.readValue(responseBody, FigmaTokenResponse.class);

            return figmaTokenResponse.getAccessToken();
        } catch (Exception e) {
            System.out.println("Error parsing JSON to FigmaTokenResponse: " + e.getMessage());
            throw new RuntimeException("Failed to parse response to FigmaTokenResponse", e);
        }
    }

    public UserInfoResponse getUserInfo(String accessToken) {
        return webClient
                .get()
                .uri("https://api.figma.com/v1/me")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(FigmaUserResponse.class)
                .map(
                        figmaUserResponse ->
                                new UserInfoResponse(
                                        figmaUserResponse.getHandle(),
                                        figmaUserResponse.getEmail(),
                                        figmaUserResponse.getImg_url(),
                                        figmaUserResponse.getId()))
                .doOnError(
                        error -> {
                            System.out.println("Error retrieving user info: " + error.getMessage());
                            throw new RuntimeException("Failed to retrieve user info", error);
                        })
                .block();
    }
}
