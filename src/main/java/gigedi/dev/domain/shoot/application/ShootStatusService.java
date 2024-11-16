package gigedi.dev.domain.shoot.application;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import gigedi.dev.domain.shoot.dao.ShootStatusRepository;
import gigedi.dev.domain.shoot.domain.ShootStatus;
import gigedi.dev.global.error.exception.CustomException;
import gigedi.dev.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShootStatusService {
    private final ShootStatusRepository shootStatusRepository;

    @Transactional
    public ShootStatus getShootStatusByShootId(Long shootId) {
        return shootStatusRepository
                .findByShoot_ShootId(shootId)
                .orElseThrow(() -> new CustomException(ErrorCode.SHOOT_STATUS_NOT_FOUND));
    }
}
