package gigedi.dev.domain.archive.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gigedi.dev.domain.archive.dao.ArchiveRepository;
import gigedi.dev.domain.archive.domain.Archive;
import gigedi.dev.global.error.exception.CustomException;
import gigedi.dev.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ArchiveService {
    private final ArchiveRepository archiveRepository;

    public Archive getArchiveById(Long archiveId) {
        return archiveRepository
                .findByArchiveIdAndDeletedAtIsNull(archiveId)
                .orElseThrow(() -> new CustomException(ErrorCode.ARCHIVE_NOT_FOUND));
    }
}
