package gigedi.dev.domain.file.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gigedi.dev.domain.file.dao.AuthorityRepository;
import gigedi.dev.domain.file.domain.Authority;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthorityService {
    private final AuthorityRepository authorityRepository;

    @Transactional(readOnly = true)
    public List<Authority> getRelatedAuthorityList(Long memberId) {
        return authorityRepository.findRelatedAuthorities(memberId);
    }
}
