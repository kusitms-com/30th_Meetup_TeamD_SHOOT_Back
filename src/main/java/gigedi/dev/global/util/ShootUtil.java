package gigedi.dev.global.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ShootUtil {
    private static final String SPACE_DELIMITER = "\\s+";
    private static final String TAG_PREFIX = "@";

    public static List<String> extractTags(String content) {

        if (content == null || content.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.stream(content.split(SPACE_DELIMITER))
                .filter(word -> word.startsWith(TAG_PREFIX))
                .map(word -> word.substring(1))
                .collect(Collectors.toList());
    }
}
