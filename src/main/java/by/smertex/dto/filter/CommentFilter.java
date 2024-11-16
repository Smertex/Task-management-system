package by.smertex.dto.filter;

import lombok.Builder;

@Builder
public record CommentFilter (UserFilter createdBy){
}
