package by.smertex.realisation.dto.filter;

import by.smertex.realisation.database.entity.enums.Priority;
import by.smertex.realisation.database.entity.enums.Status;
import jakarta.validation.Valid;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TaskUserFilter (@Valid UserFilter createdBy,
                              LocalDateTime createdAt,
                              LocalDateTime closedAt,
                              Status status,
                              Priority priority){
}
