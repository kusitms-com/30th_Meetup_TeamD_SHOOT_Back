package gigedi.dev.domain.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponse {
    private String userName;
    private String email;
    private String ImgUrl;
    private String userId;
}
