package by.smertex.realisation.dto.filter;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CommentFilter (@NotNull @Valid UserFilter createdBy){
}
