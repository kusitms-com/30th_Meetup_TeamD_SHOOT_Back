package gigedi.dev.domain.shoot.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gigedi.dev.domain.shoot.domain.Shoot;

@Repository
public interface ShootRepository extends JpaRepository<Shoot, Long> {
    List<Shoot> findAllByBlock_BlockId(Long blockId);
}
