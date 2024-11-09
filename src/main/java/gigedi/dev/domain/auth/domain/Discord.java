package gigedi.dev.domain.auth.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import gigedi.dev.domain.config.BaseTimeEntity;
import gigedi.dev.domain.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Discord extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long discordId;

    @Column(nullable = false)
    private String email;

    private LocalDateTime deletedAt;

    private String accessToken;
    private String refreshToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memeber_id")
    private Member member;

    @Builder(access = AccessLevel.PRIVATE)
    private Discord(String email, String accessToken, String refreshToken, Member member) {
        this.email = email;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.member = member;
    }

    public static Discord createDiscord(String email, Member member) {
        return Discord.builder().email(email).member(member).build();
    }
}
