package gigedi.dev.domain.figma.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gigedi.dev.domain.auth.dao.FigmaRepository;
import gigedi.dev.domain.auth.domain.Figma;
import gigedi.dev.global.error.exception.CustomException;
import gigedi.dev.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class FigmaService {
    private final FigmaRepository figmaRepository;

    public void validateFigmaAccountAlreadyExists(String figmaId) {
        if (figmaRepository.findByFigmaUserIdAndDeletedAtIsNull(figmaId).isPresent()) {
            throw new CustomException(ErrorCode.FIGMA_ACCOUNT_ALREADY_CONNECTED);
        }
    }

    public Figma getFigmaByFigmaId(String figmaId) {
        return figmaRepository
                .findByFigmaUserIdAndDeletedAtIsNull(figmaId)
                .orElseThrow(() -> new CustomException(ErrorCode.FIGMA_NOT_CONNECTED));
    }

    public Figma findByTag(String tag) {
        return figmaRepository
                .findByFigmaName(tag)
                .orElseThrow(() -> new CustomException(ErrorCode.FIGMA_USER_INFO_NOT_FOUND));
    }
}
