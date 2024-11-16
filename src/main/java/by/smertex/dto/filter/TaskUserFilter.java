package by.smertex.dto.filter;

import by.smertex.database.entity.enums.Priority;
import by.smertex.database.entity.enums.Status;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TaskUserFilter (UserFilter createdBy,
                              LocalDateTime createdAt,
                              LocalDateTime closedAt,
                              Status status,
                              Priority priority){
}
