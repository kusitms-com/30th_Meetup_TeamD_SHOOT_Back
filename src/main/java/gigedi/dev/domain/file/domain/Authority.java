package gigedi.dev.domain.file.domain;

import gigedi.dev.domain.auth.domain.Figma;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authorityId;

    @Column(nullable = false)
    private boolean alarm;

    @ManyToOne
    @JoinColumn(name = "figma_id")
    @Column(nullable = false)
    private Figma figma;

    @ManyToOne
    @JoinColumn(name = "file_id", nullable = false)
    @Column(nullable = false)
    private File file;

    @Builder(access = AccessLevel.PRIVATE)
    private Authority(boolean alarm, Figma figma, File file) {
        this.alarm = alarm;
        this.figma = figma;
        this.file = file;
    }

    public static Authority createAuthority(Figma figma, File file) {
        return Authority.builder()
                .alarm(true)
                .figma(figma)
                .file(file)
                .build();
    }

}
