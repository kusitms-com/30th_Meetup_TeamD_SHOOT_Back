package gigedi.dev.domain.auth.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gigedi.dev.domain.auth.domain.Figma;

@Repository
public interface FigmaRepository extends JpaRepository<Figma, Long> {
    Optional<Figma> findByMemberId(Long memberId);

    Optional<Figma> findByFigmaUserId(String figmaUserId);
}
