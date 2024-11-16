package by.smertex.dto.exception;

import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;

public record ResponseException(String massage,
                                HttpStatusCode httpStatusCode,
                                LocalDateTime occurrenceTime) {
}
