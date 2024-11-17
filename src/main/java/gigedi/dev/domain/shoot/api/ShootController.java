package gigedi.dev.domain.shoot.api;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gigedi.dev.domain.shoot.application.ShootService;
import gigedi.dev.domain.shoot.dto.request.UpdateShootStatusRequest;
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

    @Operation(summary = "특정 Shoot 삭제 API", description = "하나의 Shoot을 삭제하는 API")
    @DeleteMapping("/{shootId}")
    public void deleteShoot(@PathVariable Long shootId) {
        shootService.deleteShoot(shootId);
    }

    @Operation(summary = "Shoot 상태 변경 API", description = "Shoot의 상태(yet, doing, done)를 변경하는 API")
    @PatchMapping("/status/{shootId}")
    public GetShootResponse updateShootStatus(
            @PathVariable Long shootId, @RequestBody UpdateShootStatusRequest request) {
        return shootService.updateShootStatus(shootId, request.status());
    }

    @Operation(summary = "Our Shoot 조회 API", description = "내 shoot status들을 조회하는 API")
    @GetMapping("/status")
    public List<GetShootResponse> getOurShoot() {
        return shootService.getOurShoot();
    }
}
