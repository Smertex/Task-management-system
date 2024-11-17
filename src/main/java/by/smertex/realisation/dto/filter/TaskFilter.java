package by.smertex.realisation.dto.filter;

import by.smertex.realisation.database.entity.enums.Priority;
import by.smertex.realisation.database.entity.enums.Status;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;


@Builder
public record TaskFilter(@Valid @NotNull UserFilter createdBy,
                         LocalDateTime createdAt,
                         LocalDateTime closedAt,
                         @Valid @NotNull UserFilter performer,
                         Status status,
                         Priority priority,
                         String name) {
}
