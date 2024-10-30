package gigedi.dev.domain.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenPairResponse {
    private final String refreshToken;
    private final String accessToken;
}
