package gigedi.dev.domain.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FigmaAccountResponse(String figmaId, String email) {}
