package gigedi.dev.domain.member.dto.response;

import gigedi.dev.domain.member.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberInfoResponse {

    private Long id;
    private String username;

    public MemberInfoResponse(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
    }


}
