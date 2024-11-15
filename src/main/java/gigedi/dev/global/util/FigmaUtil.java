package gigedi.dev.global.util;

import static gigedi.dev.global.common.constants.HeaderConstants.FIGMA_ID_ATTRIBUTE;
import static gigedi.dev.global.common.constants.HeaderConstants.FILE_ID_ATTRIBUTE;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import gigedi.dev.domain.auth.dao.FigmaRepository;
import gigedi.dev.domain.auth.domain.Figma;
import gigedi.dev.domain.file.dao.AuthorityRepository;
import gigedi.dev.domain.file.dao.FileRepository;
import gigedi.dev.domain.file.domain.Authority;
import gigedi.dev.domain.file.domain.File;
import gigedi.dev.domain.member.domain.Member;
import gigedi.dev.global.error.exception.CustomException;
import gigedi.dev.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FigmaUtil {
    private final FigmaRepository figmaRepository;
    private final FileRepository fileRepository;
    private final AuthorityRepository authorityRepository;
    private final MemberUtil memberUtil;

    public Figma getCurrentFigma() {
        String figmaId = getCurrentAttribute(FIGMA_ID_ATTRIBUTE);
        validateId(figmaId);
        return findAndValidateFigmaId(figmaId);
    }

    public File getCurrentFile() {
        String fileId = getCurrentAttribute(FILE_ID_ATTRIBUTE);
        validateId(fileId);
        Figma currentFigma = getCurrentFigma();

        File currentFile =
                fileRepository
                        .findByFileKey(fileId)
                        .orElseGet(() -> fileRepository.save(File.createFile(fileId)));

        if (authorityRepository.findByFigmaAndFile(currentFigma, currentFile).isEmpty()) {
            authorityRepository.save(Authority.createAuthority(currentFigma, currentFile));
        }

        return currentFile;
    }

    private void validateId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new CustomException(ErrorCode.FIGMA_INFO_NOT_FOUND);
        }
    }

    private String getCurrentAttribute(String attributeName) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            throw new CustomException(ErrorCode.FIGMA_INFO_NOT_FOUND);
        }
        ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
        return (String) attributes.getAttribute(attributeName, RequestAttributes.SCOPE_REQUEST);
    }

    private Figma findAndValidateFigmaId(String figmaId) {
        final Member currentMember = memberUtil.getCurrentMember();

        Figma figma =
                figmaRepository
                        .findByFigmaUserIdAndDeletedAtIsNull(figmaId)
                        .orElseThrow(() -> new CustomException(ErrorCode.FIGMA_NOT_CONNECTED));

        if (!figma.getMember().equals(currentMember)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_FIGMA_ACCESS);
        }

        return figma;
    }
}
