package gigedi.dev.domain.file.domain;

import jakarta.persistence.Column;
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
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    @Column(nullable = false)
    private String fileKey;

    @Column(nullable = false)
    private Integer archiveCount;

    @Builder(access = AccessLevel.PRIVATE)
    private File(String fileKey, Integer archiveCount) {
        this.fileKey = fileKey;
        this.archiveCount = archiveCount;
    }

    public static File createFile(String fileKey) {
        return File.builder()
                .fileKey(fileKey)
                .archiveCount(0)
                .build();
    }
}
