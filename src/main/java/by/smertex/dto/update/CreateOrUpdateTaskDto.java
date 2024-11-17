package by.smertex.dto.update;

import by.smertex.database.entity.enums.Priority;
import by.smertex.database.entity.enums.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateOrUpdateTaskDto(@NotNull String name,
                                    @NotNull Status status,
                                    @NotNull Priority priority,
                                    @NotNull String description,
                                    @Email @NotNull String performerEmail) {
}
