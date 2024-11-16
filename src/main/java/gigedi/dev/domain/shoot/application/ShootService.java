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
        if (duration.toMinutes() < 60) {
            return duration.toMinutes() + " minutes ago";
        } else if (duration.toHours() < 24) {
            return duration.toHours() + " hours ago";
        } else {
            return duration.toDays() + " days ago";
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
