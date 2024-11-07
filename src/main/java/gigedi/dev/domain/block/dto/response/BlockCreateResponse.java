package gigedi.dev.domain.block.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BlockCreateResponse {
    private Long blockId;
    private String title;
    private double xCoordinate;
    private double yCoordinate;
    private double height;
    private double width;
}
