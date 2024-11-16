package gigedi.dev.domain.shoot.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import gigedi.dev.domain.shoot.dao.ShootRepository;
import gigedi.dev.domain.shoot.dao.ShootStatusRepository;
import gigedi.dev.domain.shoot.domain.Shoot;
import gigedi.dev.domain.shoot.domain.ShootStatus;
import gigedi.dev.domain.shoot.domain.Status;
import gigedi.dev.domain.shoot.dto.response.GetShootResponse;
import gigedi.dev.global.util.TimeUtil;
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
                            String timeAgo = TimeUtil.calculateTimeAgo(shoot.getCreatedAt());

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

    private List<GetShootResponse.User> getUsersByStatus(Shoot shoot, String status) {
        List<ShootStatus> statuses =
                shootStatusRepository.findByShoot_ShootIdAndStatus(
                        shoot.getShootId(), Status.valueOf(status));

        return statuses.stream()
                .map(
                        s ->
                                new GetShootResponse.User(
                                        s.getFigma().getFigmaId(),
                                        s.getFigma().getFigmaName(),
                                        s.getFigma().getFigmaProfile()))
                .collect(Collectors.toList());
    }
}
