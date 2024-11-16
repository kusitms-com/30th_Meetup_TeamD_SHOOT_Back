package gigedi.dev.domain.shoot.application;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import gigedi.dev.domain.shoot.dao.ShootRepository;
import gigedi.dev.domain.shoot.dao.ShootStatusRepository;
import gigedi.dev.domain.shoot.domain.Shoot;
import gigedi.dev.domain.shoot.domain.ShootStatus;
import gigedi.dev.domain.shoot.domain.Status;
import gigedi.dev.domain.shoot.dto.response.GetShootResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShootService {
    private final ShootRepository shootRepository;
    private final ShootStatusRepository shootStatusRepository;
    private static final String MINUTES_AGO = " minutes ago";
    private static final String HOURS_AGO = " hours ago";
    private static final String DAYS_AGO = " days ago";
    private static final long MINUTES_IN_AN_HOUR = 60;
    private static final long HOURS_IN_A_DAY = 24;

    public List<GetShootResponse> getShoot(Long blockId) {
        List<Shoot> shoots = shootRepository.findAllByBlock_BlockId(blockId);

        return shoots.stream()
                .map(
                        shoot -> {
                            String timeAgo = calculateTimeAgo(shoot.getCreatedAt());

                            List<GetShootResponse.User> yet = getUsersByStatus(shoot, "YET");
                            List<GetShootResponse.User> doing = getUsersByStatus(shoot, "DOING");
                            List<GetShootResponse.User> done = getUsersByStatus(shoot, "DONE");

                            return new GetShootResponse(
                                    shoot.getShootId(),
                                    shoot.getFigma().getFigmaName(),
                                    shoot.getFigma().getFigmaProfile(),
                                    shoot.getContent(),
                                    timeAgo,
                                    yet,
                                    doing,
                                    done,
                                    shoot.getCreatedAt().toString());
                        })
                .collect(Collectors.toList());
    }

    private String calculateTimeAgo(LocalDateTime createdAt) {
        Duration duration = Duration.between(createdAt, LocalDateTime.now());
        long minutes = duration.toMinutes();
        long hours = duration.toHours();
        long days = duration.toDays();

        if (minutes < MINUTES_IN_AN_HOUR) {
            return minutes + MINUTES_AGO;
        } else if (hours < HOURS_IN_A_DAY) {
            return hours + HOURS_AGO;
        } else {
            return days + DAYS_AGO;
        }
    }

    private List<GetShootResponse.User> getUsersByStatus(Shoot shoot, String status) {
        // ShootStatus를 shootId와 상태로 조회
        List<ShootStatus> statuses =
                shootStatusRepository.findByShoot_ShootIdAndStatus(
                        shoot.getShootId(), Status.valueOf(status));

        // 조회된 데이터를 User DTO로 매핑
        return statuses.stream()
                .map(
                        s ->
                                new GetShootResponse.User(
                                        s.getFigma().getFigmaId(), // Figma ID
                                        s.getFigma().getFigmaName(), // Figma 이름
                                        s.getFigma().getFigmaProfile() // Figma 프로필
                                        ))
                .collect(Collectors.toList());
    }
}
