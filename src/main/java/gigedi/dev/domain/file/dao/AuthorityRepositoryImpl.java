package gigedi.dev.domain.file.dao;

import static gigedi.dev.domain.auth.domain.QFigma.figma;
import static gigedi.dev.domain.file.domain.QAuthority.authority;
import static gigedi.dev.domain.member.domain.QMember.member;

import java.util.List;
import java.util.Optional;

import com.querydsl.jpa.impl.JPAQueryFactory;

import gigedi.dev.domain.auth.domain.Figma;
import gigedi.dev.domain.file.domain.Authority;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthorityRepositoryImpl implements AuthorityRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Authority> findRelatedAuthorities(Long memberId) {
        return queryFactory
                .selectFrom(authority)
                .join(authority.figma, figma)
                .join(figma.member, member)
                .where(
                        member.id
                                .eq(memberId)
                                .and(member.deletedAt.isNull())
                                .and(figma.deletedAt.isNull()))
                .fetch();
    }

    @Override
    public Optional<Authority> findByFileAndActiveFigma(Long fileId, List<Figma> figmaList) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(authority)
                        .where(
                                authority
                                        .file
                                        .fileId
                                        .eq(fileId)
                                        .and(authority.figma.in(figmaList))
                                        .and(authority.figma.deletedAt.isNull()))
                        .fetchOne());
    }
}
