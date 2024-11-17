package gigedi.dev.domain.file.dao;

import java.util.List;

import gigedi.dev.domain.file.domain.Authority;

public interface AuthorityRepositoryCustom {
    List<Authority> findRelatedAuthorities(Long memberId);
}
