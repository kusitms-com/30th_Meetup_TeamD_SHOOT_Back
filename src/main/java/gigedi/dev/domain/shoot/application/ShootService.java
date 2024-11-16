package gigedi.dev.domain.shoot.application;

import java.util.List;

import org.springframework.stereotype.Service;

import gigedi.dev.domain.shoot.dao.ShootRepository;
import gigedi.dev.domain.shoot.dao.ShootStatusRepository;
import gigedi.dev.domain.shoot.domain.Shoot;
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
                        shoot ->
                                GetShootResponse.from(
                                        shoot,
                                        shootStatusRepository.findByShoot_ShootIdAndStatus(
                                                shoot.getShootId(), Status.YET),
                                        shootStatusRepository.findByShoot_ShootIdAndStatus(
                                                shoot.getShootId(), Status.DOING),
                                        shootStatusRepository.findByShoot_ShootIdAndStatus(
                                                shoot.getShootId(), Status.DONE)))
                .toList();
    }
}
