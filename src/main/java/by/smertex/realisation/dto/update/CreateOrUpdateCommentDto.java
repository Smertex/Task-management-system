package by.smertex.realisation.dto.update;

import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CreateOrUpdateCommentDto(@Size(min = 1) String content) {
}
