package by.smertex.dto.filter;

import by.smertex.database.entity.Task;
import by.smertex.database.entity.User;
import lombok.Builder;

@Builder
public record CommentFilter (User createdBy,
                             Task from){
}