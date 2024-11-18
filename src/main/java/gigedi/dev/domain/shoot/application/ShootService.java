package gigedi.dev.domain.shoot.application;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gigedi.dev.domain.auth.domain.Figma;
import gigedi.dev.domain.block.application.BlockService;
import gigedi.dev.domain.block.domain.Block;
import gigedi.dev.domain.shoot.dao.ShootRepository;
import gigedi.dev.domain.shoot.dao.ShootStatusRepository;
import gigedi.dev.domain.shoot.domain.Shoot;
import gigedi.dev.domain.shoot.domain.ShootStatus;
import gigedi.dev.domain.shoot.domain.Status;
import gigedi.dev.domain.shoot.dto.response.GetShootResponse;
import gigedi.dev.global.error.exception.CustomException;
import gigedi.dev.global.error.exception.ErrorCode;
import gigedi.dev.global.util.FigmaUtil;
import gigedi.dev.global.util.ShootUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShootService {
    private final ShootRepository shootRepository;
    private final ShootStatusRepository shootStatusRepository;
    private final FigmaUtil figmaUtil;
    private final BlockService blockService;
    private final ShootTagService shootTagService;

    @Transactional(readOnly = true)
    public List<GetShootResponse> getShoot(Long blockId) {
        List<Shoot> shoots = shootRepository.findAllByBlock_BlockIdAndDeletedAtIsNull(blockId);
        return shoots.stream()
                .map(
                        shoot -> {
                            return GetShootResponse.of(
                                    shoot,
                                    getUsersByStatus(shoot, Status.YET),
                                    getUsersByStatus(shoot, Status.DOING),
                                    getUsersByStatus(shoot, Status.DONE));
                        })
                .toList();
    }

    private List<GetShootResponse.User> getUsersByStatus(Shoot shoot, Status status) {
        return shootStatusRepository
                .findByShoot_ShootIdAndStatus(shoot.getShootId(), status)
                .stream()
                .map(
                        shootStatus -> {
                            Figma figma = shootStatus.getFigma();
                            return new GetShootResponse.User(
                                    figma.getFigmaId(),
                                    figma.getFigmaName(),
                                    figma.getFigmaProfile());
                        })
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteShoot(Long shootId) {
        Shoot shoot = findValidShoot(shootId);
        shoot.deleteShoot();
    }

    @Transactional
    public GetShootResponse updateShootStatus(Long shootId, Status newStatus) {
        final Figma figma = figmaUtil.getCurrentFigma();
        validateStatus(newStatus);
        Shoot shoot = findValidShoot(shootId);
        ShootStatus shootStatus =
                shootStatusRepository
                        .findByShoot_ShootIdAndFigma_FigmaId(shootId, figma.getFigmaId())
                        .orElseGet(
                                () -> {
                                    ShootStatus newShootStatus =
                                            ShootStatus.createShootStatus(newStatus, figma, shoot);
                                    return shootStatusRepository.save(newShootStatus);
                                });

        if (shootStatus.getStatus() != newStatus) {
            shootStatus.updateStatus(newStatus);
        }
        return GetShootResponse.of(
                shoot,
                getUsersByStatus(shoot, Status.YET),
                getUsersByStatus(shoot, Status.DOING),
                getUsersByStatus(shoot, Status.DONE));
    }

    private void validateStatus(Status status) {
        if (status == null || !EnumSet.allOf(Status.class).contains(status)) {
            throw new CustomException(ErrorCode.INVALID_STATUS);
        }
    }

    @Transactional
    public Shoot findValidShoot(Long shootId) {
        return shootRepository
                .findByShootIdAndDeletedAtIsNull(shootId)
                .orElseThrow(() -> new CustomException(ErrorCode.SHOOT_NOT_FOUND));
    }

    public GetShootResponse createShoot(Long blockId, String content) {
        Block block = blockService.getBlockById(blockId);
        final Figma figma = figmaUtil.getCurrentFigma();
        Shoot shoot = Shoot.createShoot(content, figma, block);
        shootRepository.save(shoot);
        processTags(content, shoot);

        return GetShootResponse.of(
                shoot,
                getUsersByStatus(shoot, Status.YET),
                getUsersByStatus(shoot, Status.DOING),
                getUsersByStatus(shoot, Status.DONE));
    }

    private void processTags(String content, Shoot shoot) {
        List<String> tags = ShootUtil.extractTags(content);
        shootTagService.createShootTags(shoot, tags);
    }
}
