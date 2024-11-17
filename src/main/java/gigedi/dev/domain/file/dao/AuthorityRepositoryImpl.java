package gigedi.dev.domain.file.dao;

import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;

import gigedi.dev.domain.auth.domain.QFigma;
import gigedi.dev.domain.file.domain.Authority;
import gigedi.dev.domain.file.domain.QAuthority;
import gigedi.dev.domain.member.domain.QMember;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthorityRepositoryImpl implements AuthorityRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Authority> findRelatedAuthorities(Long memberId) {
        return queryFactory
                .selectFrom(QAuthority.authority)
                .join(QAuthority.authority.figma, QFigma.figma)
                .join(QFigma.figma.member, QMember.member)
                .where(
                        QMember.member
                                .id
                                .eq(memberId)
                                .and(QMember.member.deletedAt.isNull())
                                .and(QFigma.figma.deletedAt.isNull()))
                .fetch();
    }
}
