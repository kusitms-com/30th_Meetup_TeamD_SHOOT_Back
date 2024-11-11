package gigedi.dev.domain.archive.application;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gigedi.dev.domain.archive.dao.ArchiveRepository;
import gigedi.dev.global.common.constants.FigmaConstants;
import gigedi.dev.global.error.exception.CustomException;
import gigedi.dev.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArchiveTitleService {
    private final ArchiveRepository archiveRepository;
    private static final int MAX_TITLE_LENGTH = 30;

    @Transactional(readOnly = true)
    public String generateUniqueTitle(String newTitle) {
        validateTitleLength(newTitle);
        List<String> existingTitles = archiveRepository.findTitlesByBaseExact(newTitle);
        return findUniqueTitle(newTitle, existingTitles);
    }

    private void validateTitleLength(String title) {
        if (title.length() > MAX_TITLE_LENGTH) {
            throw new CustomException(ErrorCode.ARCHIVE_TITLE_EXCEED_LIMIT);
        }
    }

    private String findUniqueTitle(String newTitle, List<String> existingTitles) {
        int maxNumber = findMaxNumber(newTitle, existingTitles);
        String baseTitle = newTitle;
        int nextNumber = maxNumber + 1;

        while (true) {
            String suffix = String.valueOf(nextNumber);
            int suffixLength = suffix.length();

            while (baseTitle.length() + suffixLength > MAX_TITLE_LENGTH) {
                baseTitle = baseTitle.substring(0, baseTitle.length() - 1);
            }

            String uniqueTitle = baseTitle + suffix;
            if (!existingTitles.contains(uniqueTitle)) {
                return uniqueTitle;
            }

            nextNumber++;
        }
    }

    private int findMaxNumber(String newTitle, List<String> existingTitles) {
        int maxNumber = 0;
        Pattern pattern =
                Pattern.compile(Pattern.quote(newTitle) + FigmaConstants.ARCHIVE_TITLE_REGEX);
        for (String title : existingTitles) {
            Matcher matcher = pattern.matcher(title);
            if (matcher.matches()) {
                int number = Integer.parseInt(matcher.group(1));
                maxNumber = Math.max(maxNumber, number);
            }
        }
        return maxNumber;
    }
}
