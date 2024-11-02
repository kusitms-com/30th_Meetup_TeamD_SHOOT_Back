package gigedi.dev.domain.shoot.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
    YET("YET"),
    DOING("DOING"),
    DONE("DONE");

    private final String value;
}
