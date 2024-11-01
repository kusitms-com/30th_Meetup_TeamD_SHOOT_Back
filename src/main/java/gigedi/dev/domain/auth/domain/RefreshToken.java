package gigedi.dev.domain.auth.domain;

import jakarta.persistence.*;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {
    @Id
    private Long memberId;
    private String token;

    @Builder
    private RefreshToken(Long memberId, String token) {
        this.memberId = memberId;
        this.token = token;
    }

    public static RefreshToken of(Long memberId, String token) {
        return RefreshToken.builder().memberId(memberId).token(token).build();
    }

    public void updateRefreshToken(String newToken) {
        this.token = newToken;
    }
}
