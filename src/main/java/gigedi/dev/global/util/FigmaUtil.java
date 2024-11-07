package gigedi.dev.global.util;

import static gigedi.dev.global.common.constants.FigmaConstants.FIGMA_ID_ATTRIBUTE;
import static gigedi.dev.global.common.constants.FigmaConstants.FILE_ID_ATTRIBUTE;

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
        String figmaId = getCurrentFigmaId();
        if (figmaId == null || figmaId.trim().isEmpty()) {
            throw new CustomException(ErrorCode.FIGMA_INFO_NOT_FOUND);
        }
        return findAndValidateFigmaId(figmaId);
    }

    public File getCurrentFile() {
        String fileId = getCurrentFileId();
        if (fileId == null || fileId.trim().isEmpty()) {
            throw new CustomException(ErrorCode.FIGMA_INFO_NOT_FOUND);
        }
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

    private String getCurrentFigmaId() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            throw new CustomException(ErrorCode.FIGMA_INFO_NOT_FOUND);
        }
        ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
        return (String)
                attributes.getAttribute(FIGMA_ID_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
    }

    private String getCurrentFileId() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            throw new CustomException(ErrorCode.FIGMA_INFO_NOT_FOUND);
        }
        ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
        return (String) attributes.getAttribute(FILE_ID_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
    }

    private Figma findAndValidateFigmaId(String figmaId) {
        final Member currentMember = memberUtil.getCurrentMember();

        Figma figma =
                figmaRepository
                        .findByFigmaUserId(figmaId)
                        .orElseThrow(() -> new CustomException(ErrorCode.FIGMA_NOT_CONNECTED));

        if (!figma.getMember().equals(currentMember)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_FIGMA_ACCESS);
        }

        return figma;
    }
}
