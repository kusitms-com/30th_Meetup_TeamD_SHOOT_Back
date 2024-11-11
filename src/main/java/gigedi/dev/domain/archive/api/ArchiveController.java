package gigedi.dev.domain.archive.api;

import org.springframework.web.bind.annotation.*;

import gigedi.dev.domain.archive.application.ArchiveService;
import gigedi.dev.domain.archive.dto.request.ArchiveCreateRequest;
import gigedi.dev.domain.archive.dto.response.ArchiveInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Archive", description = "Archive 관련 API")
@RestController
@RequestMapping("/api/v1/archive")
@RequiredArgsConstructor
public class ArchiveController {
    private final ArchiveService archiveService;

    @Operation(summary = "아카이브 생성", description = "아카이브 생성을 진행하는 API")
    @PostMapping
    public ArchiveInfoResponse createArchive(@RequestBody ArchiveCreateRequest request) {
        return archiveService.createArchive(request);
    }
}
