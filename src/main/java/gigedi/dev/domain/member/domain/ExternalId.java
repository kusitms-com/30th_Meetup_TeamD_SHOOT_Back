package gigedi.dev.domain.member.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExternalId {

    private String slackId;
    private String discordId;

    @Builder(access = AccessLevel.PRIVATE)
    private ExternalId(String slackId, String discordId) {
        this.slackId = slackId;
        this.discordId = discordId;
    }

    public static ExternalId createExternalId(
            String slackId, String discordId) {
        return ExternalId.builder()
                .slackId(slackId)
                .discordId(discordId)
                .build();
    }
}
