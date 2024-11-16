package gigedi.dev.domain.shoot.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import gigedi.dev.domain.shoot.domain.Shoot;
import gigedi.dev.domain.shoot.domain.ShootStatus;
import gigedi.dev.global.util.TimeUtil;

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

    public static GetShootResponse from(
            Shoot shoot,
            List<ShootStatus> yetStatuses,
            List<ShootStatus> doingStatuses,
            List<ShootStatus> doneStatuses) {
        return new GetShootResponse(
                shoot.getShootId(),
                shoot.getFigma().getFigmaName(),
                shoot.getFigma().getFigmaProfile(),
                shoot.getContent(),
                TimeUtil.calculateTimeAgo(shoot.getCreatedAt()),
                convertToUsers(yetStatuses),
                convertToUsers(doingStatuses),
                convertToUsers(doneStatuses),
                shoot.getCreatedAt().toString());
    }

    private static List<User> convertToUsers(List<ShootStatus> statuses) {
        return statuses.stream()
                .map(
                        status ->
                                new User(
                                        status.getFigma().getFigmaId(),
                                        status.getFigma().getFigmaName(),
                                        status.getFigma().getFigmaProfile()))
                .collect(Collectors.toList());
    }
}
