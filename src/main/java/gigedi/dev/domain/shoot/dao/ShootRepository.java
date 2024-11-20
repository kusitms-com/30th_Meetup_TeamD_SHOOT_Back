package gigedi.dev.domain.shoot.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import gigedi.dev.domain.auth.domain.Figma;
import gigedi.dev.domain.shoot.domain.Shoot;
import gigedi.dev.domain.shoot.domain.Status;

@Repository
public interface ShootRepository extends JpaRepository<Shoot, Long> {
    List<Shoot> findAllByBlock_BlockIdAndDeletedAtIsNull(Long blockId);

    Optional<Shoot> findByShootIdAndDeletedAtIsNull(Long shootId);

    List<Shoot> findByFigmaAndStatusAndDeletedAtIsNull(Figma figma, Status status);

    @Query(
            "SELECT st.shoot FROM ShootTag st "
                    + "WHERE st.figma = :figma AND st.isRead = false AND st.shoot.deletedAt IS NULL")
    List<Shoot> findMentionedShootsByFigma(Figma figma);
}
