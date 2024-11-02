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
import gigedi.dev.domain.block.dto.response.BlockCreateResponse;
import gigedi.dev.domain.member.domain.Member;
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
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "Figma 정보를 찾을 수 없습니다. 회원 ID: "
                                                        + currentMember.getId()));

        Archive archive =
                archiveRepository
                        .findById(archiveId)
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "해당 Archive를 찾을 수 없습니다. ID: " + archiveId));

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
}
