package by.smertex.dto;

import by.smertex.database.entity.User;
import by.smertex.database.entity.enums.Priority;
import by.smertex.database.entity.enums.Status;
import lombok.Builder;


@Builder
public record TaskFilter(User createdBy,
                         String createdAt,
                         String closedAt,
                         User performer,
                         Status status,
                         Priority priority) {
}
