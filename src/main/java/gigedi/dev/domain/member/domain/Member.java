package gigedi.dev.domain.member.domain;

import gigedi.dev.global.common.model.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String username;

    @Embedded private ExternalId externalId;

    @Builder(access = AccessLevel.PRIVATE)
    private Member(
            String username, ExternalId externalId
    ) {
        this.username = username;
        this.externalId = externalId;
    }

    public static Member createMember(
            String username, ExternalId externalId
    ) {
        return Member.builder()
                .username(username)
                .externalId(externalId)
                .build();
    }





}
