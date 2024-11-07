package gigedi.dev.domain.archive.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gigedi.dev.domain.archive.domain.Archive;

@Repository
public interface ArchiveRepository extends JpaRepository<Archive, Long> {
    Optional<Archive> findByArchiveIdAndDeletedAtIsNull(Long archiveId);
}
