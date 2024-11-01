package gigedi.dev.domain.block.domain;

import gigedi.dev.domain.archive.domain.Archive;
import gigedi.dev.domain.auth.domain.Figma;
import gigedi.dev.global.common.model.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Block extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long blockId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer shootCount;

    @Column(nullable = false)
    private Float xCoordinate;

    @Column(nullable = false)
    private Float yCoordinate;

    @Column(nullable = false)
    private Float height;

    @Column(nullable = false)
    private Float width;

    @Column(nullable = false)

    private LocalDateTime deletedAt;

    @ManyToOne
    @JoinColumn(name = "archive_id", nullable = false)
    @Column(nullable = false)
    private Archive archive;

    @ManyToOne
    @JoinColumn(name = "figma_id", nullable = false)
    @Column(nullable = false)
    private Figma figma;

    @Builder(access = AccessLevel.PRIVATE)
    private Block(String title, Integer shootCount, Float xCoordinate, Float yCoordinate, Float height, Float width, Archive archive, Figma figma) {
        this.title = title;
        this.shootCount = shootCount;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.height = height;
        this.width = width;
        this.archive = archive;
        this.figma = figma;
    }

    public static Block createBlock(String title, Float xCoordinate, Float yCoordinate, Float height, Float width, Archive archive, Figma figma) {
        return Block.builder()
                .title(title)
                .shootCount(0)
                .xCoordinate(xCoordinate)
                .yCoordinate(yCoordinate)
                .height(height)
                .width(width)
                .archive(archive)
                .figma(figma)
                .build();
    }
}
