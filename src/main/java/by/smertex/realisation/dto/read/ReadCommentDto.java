package by.smertex.realisation.dto.read;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReadCommentDto(String content,
                             ReadUserDto createdBy,
                             LocalDateTime createdAt) {
}
