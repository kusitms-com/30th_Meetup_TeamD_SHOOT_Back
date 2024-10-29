package gigedi.dev.domain.member.domain;

import jakarta.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class OauthInfo {
    private String googleId;
    private String googleEmail;
    private String username;
    private String profileImg;

    @Builder(access = AccessLevel.PRIVATE)
    private OauthInfo(String googleId, String googleEmail, String username, String profileImg) {
        this.googleId = googleId;
        this.googleEmail = googleEmail;
        this.username = username;
        this.profileImg = profileImg;
    }

    public static OauthInfo of(
            String googleId, String googleEmail, String username, String profileImg) {
        return OauthInfo.builder()
                .googleId(googleId)
                .googleEmail(googleEmail)
                .username(username)
                .profileImg(profileImg)
                .build();
    }
}
