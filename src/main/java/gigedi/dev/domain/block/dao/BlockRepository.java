package gigedi.dev.domain.block.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gigedi.dev.domain.block.domain.Block;

@Repository
public interface BlockRepository extends JpaRepository<Block, Long> {
    List<Block> findByArchive_ArchiveIdAndDeletedAtIsNull(Long archiveId);

    Optional<Block> findByBlockIdAndDeletedAtIsNull(Long blockId);
}
