package gigedi.dev.global.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ShootUtil {
    public static List<String> extractTags(String content) {
        if (content == null || content.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.stream(content.split(" "))
                .filter(word -> word.startsWith("@"))
                .map(word -> word.substring(1))
                .collect(Collectors.toList());
    }
}
