package gigedi.dev.domain.auth.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import gigedi.dev.domain.member.domain.Member;
import gigedi.dev.global.common.model.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Figma extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long figmaId;

    @Column(nullable = false)
    private String figmaName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String figmaProfile;

    @Column(nullable = false)
    private String figmaUserId;

    private String accessToken;
    private String refreshToken;

    @ManyToOne
    @JoinColumn(name = "memeber_id")
    private Member member;

    @Builder(access = AccessLevel.PRIVATE)
    private Figma(
            String figmaName,
            String email,
            String figmaProfile,
            String figmaUserId,
            String accessToken,
            String refreshToken,
            Member member) {
        this.figmaName = figmaName;
        this.email = email;
        this.figmaProfile = figmaProfile;
        this.figmaUserId = figmaUserId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.member = member;
    }

    public static Figma createFigma(
            String figmaName,
            String email,
            String figmaProfile,
            String figmaUserId,
            Member member) {
        return Figma.builder()
                .figmaName(figmaName)
                .email(email)
                .figmaProfile(figmaProfile)
                .figmaUserId(figmaUserId)
                .member(member)
                .build();
    }
}
