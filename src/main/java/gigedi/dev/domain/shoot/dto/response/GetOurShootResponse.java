package gigedi.dev.domain.shoot.dto.response;

import gigedi.dev.domain.shoot.domain.Shoot;
import gigedi.dev.global.util.TimeUtil;

public record GetOurShootResponse(
        Long shootId,
        String BlockTitle,
        String authorName,
        String content,
        String timeAgo,
        String createdAt) {
    public static GetOurShootResponse of(Shoot shoot) {
        return new GetOurShootResponse(
                shoot.getShootId(),
                shoot.getBlock().getTitle(),
                shoot.getFigma().getFigmaName(),
                shoot.getContent(),
                TimeUtil.calculateTimeAgo(shoot.getCreatedAt()),
                shoot.getCreatedAt().toString());
    }
}
