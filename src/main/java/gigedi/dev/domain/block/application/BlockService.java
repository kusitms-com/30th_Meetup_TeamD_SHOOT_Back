package gigedi.dev.domain.block.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gigedi.dev.domain.archive.dao.ArchiveRepository;
import gigedi.dev.domain.archive.domain.Archive;
import gigedi.dev.domain.auth.dao.FigmaRepository;
import gigedi.dev.domain.auth.domain.Figma;
import gigedi.dev.domain.block.dao.BlockRepository;
import gigedi.dev.domain.block.domain.Block;
import gigedi.dev.domain.block.dto.request.BlockCreateRequest;
import gigedi.dev.domain.block.dto.request.BlockUpdateRequest;
import gigedi.dev.domain.block.dto.response.BlockCreateResponse;
import gigedi.dev.domain.block.dto.response.GetBlockResponse;
import gigedi.dev.global.error.exception.CustomException;
import gigedi.dev.global.error.exception.ErrorCode;
import gigedi.dev.global.util.FigmaUtil;
import gigedi.dev.global.util.MemberUtil;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BlockService {
    private final MemberUtil memberUtil;
    private final ArchiveRepository archiveRepository;
    private final BlockRepository blockRepository;
    private final FigmaRepository figmaRepository;
    private final FigmaUtil figmaUtil;

    public BlockCreateResponse createBlock(Long archiveId, BlockCreateRequest request) {
        final Figma figma = figmaUtil.getCurrentFigma();
        Archive archive = getArchiveById(archiveId);
        archive.increaseBlockCount();

        Block block =
                blockRepository.save(
                        Block.createBlock(
                                request.getTitle(),
                                request.getXCoordinate(),
                                request.getYCoordinate(),
                                request.getHeight(),
                                request.getWidth(),
                                archive,
                                figma));

        return new BlockCreateResponse(
                block.getBlockId(),
                block.getTitle(),
                block.getXCoordinate(),
                block.getYCoordinate(),
                block.getHeight(),
                block.getWidth());
    }

    public List<GetBlockResponse> getBlock(Long archiveId) {
        List<GetBlockResponse> blocks =
                blockRepository.findByArchive_ArchiveId(archiveId).stream()
                        .map(
                                block ->
                                        new GetBlockResponse(
                                                block.getBlockId(),
                                                block.getTitle(),
                                                block.getShootCount()))
                        .collect(Collectors.toList());
        return blocks;
    }

    public BlockCreateResponse updateBlockTitle(Long blockId, BlockUpdateRequest request) {
        final Figma figma = figmaUtil.getCurrentFigma();

        Block block =
                blockRepository
                        .findById(blockId)
                        .orElseThrow(() -> new CustomException(ErrorCode.BLOCK_NOT_FOUND));

        block.setTitle(request.getTitle());

        return new BlockCreateResponse(
                block.getBlockId(),
                block.getTitle(),
                block.getXCoordinate(),
                block.getYCoordinate(),
                block.getHeight(),
                block.getWidth());
    }

    public void deleteBlock(Long blockId) {
        final Figma figma = figmaUtil.getCurrentFigma();

        Block block =
                blockRepository
                        .findById(blockId)
                        .orElseThrow(() -> new CustomException(ErrorCode.BLOCK_NOT_FOUND));

        blockRepository.delete(block);
    }

    public Archive getArchiveById(Long archiveId) {
        return archiveRepository
                .findById(archiveId)
                .orElseThrow(() -> new CustomException(ErrorCode.ARCHIVE_NOT_FOUND));
    }
}
