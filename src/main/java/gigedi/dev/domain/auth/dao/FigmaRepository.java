package gigedi.dev.domain.auth.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gigedi.dev.domain.auth.domain.Figma;
import gigedi.dev.domain.member.domain.Member;

@Repository
public interface FigmaRepository extends JpaRepository<Figma, Long> {
    Optional<Figma> findByMemberId(Long memberId);

    List<Figma> findByMemberAndDeletedAtIsNull(Member member);

    Optional<Figma> findByIdAndDeletedAtIsNull(Long figmaId);

    Optional<Figma> findByFigmaUserId(String figmaUserId);
}
