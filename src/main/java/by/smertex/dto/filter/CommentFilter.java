package by.smertex.dto.filter;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CommentFilter (@NotNull UserFilter createdBy){
}
