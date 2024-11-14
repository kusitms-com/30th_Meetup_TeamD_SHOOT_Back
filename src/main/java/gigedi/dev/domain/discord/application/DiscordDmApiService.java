package gigedi.dev.domain.discord.application;

import static gigedi.dev.global.common.constants.SecurityConstants.*;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import gigedi.dev.domain.discord.dto.response.CreateDMChannelResponse;
import gigedi.dev.global.error.exception.CustomException;
import gigedi.dev.global.error.exception.ErrorCode;
import gigedi.dev.infra.config.oauth.DiscordProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiscordDmApiService {
    private final RestClient restClient;
    private final DiscordProperties discordProperties;

    public CreateDMChannelResponse createDMChannel(String userId) {
        try {
            Map<String, Object> requestBody = Map.of("recipient_id", userId);

            return restClient
                    .post()
                    .uri(DISCORD_CREATE_DM_CHANNEL_URL)
                    .header(
                            HttpHeaders.AUTHORIZATION,
                            BOT_TOKEN_PREFIX + discordProperties.botToken())
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(requestBody)
                    .retrieve()
                    .onStatus(
                            status -> !status.is2xxSuccessful(),
                            (request, response) -> {
                                log.error("Discord DM 채널 생성 실패: {}", response.getStatusCode());
                                throw new CustomException(
                                        ErrorCode.DISCORD_DM_CHANNEL_CREATION_FAILED);
                            })
                    .body(CreateDMChannelResponse.class);
        } catch (Exception e) {
            log.error("Discord DM 채널 생성 중 예외 발생: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.DISCORD_DM_CHANNEL_CREATION_FAILED);
        }
    }
}
