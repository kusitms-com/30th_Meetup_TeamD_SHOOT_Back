package gigedi.dev.domain.shoot.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gigedi.dev.domain.shoot.domain.ShootTag;

@Repository
public interface ShootTagRepository extends JpaRepository<ShootTag, Long> {
    Optional<ShootTag> findByShoot_ShootIdAndFigma_FigmaId(Long shootId, Long figmaId);
}
