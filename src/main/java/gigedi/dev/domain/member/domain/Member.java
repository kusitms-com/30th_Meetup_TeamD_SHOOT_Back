package gigedi.dev.domain.member.domain;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import gigedi.dev.global.common.model.BaseTimeEntity;
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

    @Embedded private OauthInfo oauthInfo;

    private LocalDateTime deletedAt;
    private LocalDateTime lastLoginAt;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Builder(access = AccessLevel.PRIVATE)
    private Member(OauthInfo oauthInfo, LocalDateTime lastLoginAt, MemberRole role) {
        this.oauthInfo = oauthInfo;
        this.lastLoginAt = lastLoginAt;
        this.role = role;
    }

    public static Member createMember(OauthInfo oauthInfo) {
        return Member.builder()
                .oauthInfo(oauthInfo)
                .lastLoginAt(LocalDateTime.now())
                .role(MemberRole.USER)
                .build();
    }
}
