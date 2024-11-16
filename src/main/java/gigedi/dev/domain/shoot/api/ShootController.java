package gigedi.dev.domain.shoot.api;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gigedi.dev.domain.shoot.application.ShootService;
import gigedi.dev.domain.shoot.dto.response.GetShootResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Shoot", description = "Shoot 관련 API")
@RestController
@RequestMapping("/api/v1/shoot")
@RequiredArgsConstructor
public class ShootController {
    private final ShootService shootService;

    @Operation(summary = "Block 별 Shoot 조회 API", description = "Block 별 Shoot을 조회하는 API")
    @GetMapping("/{blockId}")
    public List<GetShootResponse> getBlocksByArchiveId(@PathVariable Long blockId) {
        return shootService.getShoot(blockId);
    }
}
