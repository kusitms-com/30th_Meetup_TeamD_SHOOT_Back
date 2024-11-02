package gigedi.dev.domain.archive.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import gigedi.dev.domain.archive.domain.Archive;

public interface ArchiveRepository extends JpaRepository<Archive, Long> {}
