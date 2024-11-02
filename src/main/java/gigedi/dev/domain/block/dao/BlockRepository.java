package gigedi.dev.domain.block.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import gigedi.dev.domain.block.domain.Block;

public interface BlockRepository extends JpaRepository<Block, Long> {}
