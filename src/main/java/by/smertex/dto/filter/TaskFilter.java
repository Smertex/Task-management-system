package by.smertex.dto.filter;

import by.smertex.database.entity.enums.Priority;
import by.smertex.database.entity.enums.Status;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;


@Builder
public record TaskFilter(@NotNull UserFilter createdBy,
                         LocalDateTime createdAt,
                         LocalDateTime closedAt,
                         @NotNull UserFilter performer,
                         Status status,
                         Priority priority,
                         String name) {
}
