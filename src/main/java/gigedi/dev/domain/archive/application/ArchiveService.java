package gigedi.dev.domain.archive.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gigedi.dev.domain.archive.dao.ArchiveRepository;
import gigedi.dev.domain.archive.domain.Archive;
import gigedi.dev.domain.archive.dto.request.CreateArchiveRequest;
import gigedi.dev.domain.archive.dto.request.UpdateArchiveRequest;
import gigedi.dev.domain.archive.dto.response.ArchiveInfoResponse;
import gigedi.dev.domain.auth.domain.Figma;
import gigedi.dev.domain.file.domain.File;
import gigedi.dev.global.common.constants.FigmaConstants;
import gigedi.dev.global.error.exception.CustomException;
import gigedi.dev.global.error.exception.ErrorCode;
import gigedi.dev.global.util.FigmaUtil;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ArchiveService {
    private final ArchiveRepository archiveRepository;
    private final ArchiveTitleService archiveTitleService;
    private final FigmaUtil figmaUtil;

    public ArchiveInfoResponse createArchive(CreateArchiveRequest request) {
        File currentFile = figmaUtil.getCurrentFile();
        Figma currentFigma = figmaUtil.getCurrentFigma();

        if (currentFile.getArchiveCount() >= FigmaConstants.MaxCount) {
            throw new CustomException(ErrorCode.ARCHIVE_EXCEED_LIMIT);
        }

        String archiveTitle = request.archiveTitle();

        if (archiveTitle == null || archiveTitle.trim().isEmpty()) {
            archiveTitle = FigmaConstants.NEW_ARCHIVE_NAME;
        }

        archiveTitle = archiveTitleService.generateUniqueTitle(archiveTitle);
        Archive createdArchive =
                archiveRepository.save(
                        Archive.createArchive(archiveTitle, currentFile, currentFigma));
        currentFile.increaseArchiveCount();
        return ArchiveInfoResponse.from(createdArchive);
    }

    public List<ArchiveInfoResponse> getArchiveList() {
        File currentFile = figmaUtil.getCurrentFile();
        return archiveRepository.findByFileAndDeletedAtIsNull(currentFile).stream()
                .map(ArchiveInfoResponse::from)
                .collect(Collectors.toList());
    }

    public ArchiveInfoResponse updateArchiveTitle(Long archiveId, UpdateArchiveRequest request) {
        String newTitle = archiveTitleService.generateUniqueTitle(request.archiveTitle());
        Archive archive = getArchiveById(archiveId);
        validateArchiveExistInFile(archive);
        archive.updateArchive(newTitle);
        return ArchiveInfoResponse.from(archive);
    }

    private void validateArchiveExistInFile(Archive archive) {
        File currentFile = figmaUtil.getCurrentFile();
        if (!archive.getFile().equals(currentFile)) {
            throw new CustomException(ErrorCode.ARCHIVE_NOT_EXIST_IN_FILE);
        }
    }

    public Archive getArchiveById(Long archiveId) {
        return archiveRepository
                .findByArchiveIdAndDeletedAtIsNull(archiveId)
                .orElseThrow(() -> new CustomException(ErrorCode.ARCHIVE_NOT_FOUND));
    }
}
