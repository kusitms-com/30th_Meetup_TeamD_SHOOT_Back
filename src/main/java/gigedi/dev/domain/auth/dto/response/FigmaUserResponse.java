package gigedi.dev.domain.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FigmaUserResponse {
    private String id;
    private String email;
    private String handle; // 사용자 이름
    private String img_url; // 프로필 이미지 URL
}
