package gigedi.dev.domain.block.dto.response;

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
}
