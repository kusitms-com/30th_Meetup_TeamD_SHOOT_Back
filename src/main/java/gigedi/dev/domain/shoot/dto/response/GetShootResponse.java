package gigedi.dev.domain.shoot.dto.response;

import java.util.List;

public record GetShootResponse(
        Long shootId,
        String authorName,
        String authorProfileImg,
        String content,
        String timeAgo,
        List<User> yet,
        List<User> doing,
        List<User> done,
        String createdAt) {
    public record User(Long userId, String username, String profileImg) {}
}
