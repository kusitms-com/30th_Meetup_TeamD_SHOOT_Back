package gigedi.dev.domain.block.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BlockCreateResponse {
    private Long blockId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("x_coordinate")
    private double xCoordinate;

    @JsonProperty("y_coordinate")
    private double yCoordinate;

    private double height;
    private double width;
}
