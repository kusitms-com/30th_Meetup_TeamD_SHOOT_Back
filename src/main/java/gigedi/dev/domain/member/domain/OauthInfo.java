package gigedi.dev.domain.member.domain;

import jakarta.persistence.Embeddable;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class OauthInfo {
    private String oauthId;
    private String username;
    private String profile;

    @Builder
    private OauthInfo(String oauthId, String username, String profile) {
        this.oauthId = oauthId;
        this.username = username;
        this.profile = profile;
    }

    public static OauthInfo of(String oauthId, String username, String profile) {
        return OauthInfo.builder().oauthId(oauthId).username(username).profile(profile).build();
    }
}
