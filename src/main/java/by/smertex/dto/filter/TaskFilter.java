package by.smertex.dto.filter;

import by.smertex.database.entity.enums.Priority;
import by.smertex.database.entity.enums.Status;
import lombok.Builder;

import java.time.LocalDateTime;


@Builder
public record TaskFilter(UserFilter createdBy,
                         LocalDateTime createdAt,
                         LocalDateTime closedAt,
                         UserFilter performer,
                         Status status,
                         Priority priority) {
}
