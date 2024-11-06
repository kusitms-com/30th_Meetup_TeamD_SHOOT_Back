package gigedi.dev.domain.block.application;

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
import gigedi.dev.domain.member.domain.Member;
import gigedi.dev.global.error.exception.CustomException;
import gigedi.dev.global.error.exception.ErrorCode;
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

    public BlockCreateResponse createBlock(Long archiveId, BlockCreateRequest request) {
        final Member currentMember = memberUtil.getCurrentMember();
        Figma figma =
                figmaRepository
                        .findByMemberId(currentMember.getId())
                        .orElseThrow(() -> new CustomException(ErrorCode.FIGMA_NOT_FOUND));

        Archive archive =
                archiveRepository
                        .findById(archiveId)
                        .orElseThrow(() -> new CustomException(ErrorCode.ARCHIVE_NOT_FOUND));

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

    public BlockCreateResponse getBlock(Long archiveId) {
        final Member currentMember = memberUtil.getCurrentMember();
        Figma figma =
                figmaRepository
                        .findByMemberId(currentMember.getId())
                        .orElseThrow(() -> new CustomException(ErrorCode.FIGMA_NOT_FOUND));

        Archive archive =
                archiveRepository
                        .findById(archiveId)
                        .orElseThrow(() -> new CustomException(ErrorCode.ARCHIVE_NOT_FOUND));

        Block block =
                blockRepository
                        .findByArchiveAndFigma(archive, figma)
                        .orElseThrow(() -> new CustomException(ErrorCode.BLOCK_NOT_FOUND));

        return new BlockCreateResponse(
                block.getBlockId(),
                block.getTitle(),
                block.getXCoordinate(),
                block.getYCoordinate(),
                block.getHeight(),
                block.getWidth());
    }

    public BlockCreateResponse updateBlockTitle(Long blockId, BlockUpdateRequest request) {
        final Member currentMember = memberUtil.getCurrentMember();
        Figma figma =
                figmaRepository
                        .findByMemberId(currentMember.getId())
                        .orElseThrow(() -> new CustomException(ErrorCode.FIGMA_NOT_FOUND));

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
        final Member currentMember = memberUtil.getCurrentMember();
        Figma figma =
                figmaRepository
                        .findByMemberId(currentMember.getId())
                        .orElseThrow(() -> new CustomException(ErrorCode.FIGMA_NOT_FOUND));

        Block block =
                blockRepository
                        .findById(blockId)
                        .orElseThrow(() -> new CustomException(ErrorCode.BLOCK_NOT_FOUND));

        blockRepository.delete(block);
    }
}
