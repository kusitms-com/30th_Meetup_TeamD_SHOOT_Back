package gigedi.dev.domain.block.dto.response;

import gigedi.dev.domain.block.domain.Block;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetBlockResponse {
    private Long blockId;
    private String title;
    private int shootCount;
    private double xCoordinate;
    private double yCoordinate;
    private double height;
    private double width;

    public GetBlockResponse(Block block) {
        this.blockId = block.getBlockId();
        this.title = block.getTitle();
        this.shootCount = block.getShootCount();
        this.xCoordinate = block.getXCoordinate();
        this.yCoordinate = block.getYCoordinate();
        this.height = block.getHeight();
        this.width = block.getWidth();
    }
}
