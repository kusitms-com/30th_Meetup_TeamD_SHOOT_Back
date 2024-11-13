package gigedi.dev.domain.discord.domain;

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

    @Column(nullable = false)
    private String dmChannel;

    @Column(nullable = false)
    private String refreshToken;

    @Column(nullable = false)
    private String discordUserId;

    @Column(nullable = false)
    private String guildId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder(access = AccessLevel.PRIVATE)
    private Discord(
            Member member,
            String email,
            String refreshToken,
            String discordUserId,
            String dmChannel,
            String guildId) {
        this.member = member;
        this.email = email;
        this.discordUserId = discordUserId;
        this.refreshToken = refreshToken;
        this.dmChannel = dmChannel;
        this.guildId = guildId;
    }

    public static Discord createDiscord(
            Member member,
            String email,
            String refreshToken,
            String discordUserId,
            String dmChannel,
            String guildId) {
        return Discord.builder()
                .member(member)
                .email(email)
                .discordUserId(discordUserId)
                .refreshToken(refreshToken)
                .dmChannel(dmChannel)
                .guildId(guildId)
                .build();
    }
}
