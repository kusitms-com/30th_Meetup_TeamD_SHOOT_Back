package gigedi.dev.domain.block.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateBlockRequest {
    private String title;
    private double xCoordinate;
    private double yCoordinate;
    private double height;
    private double width;
}
