package by.smertex.service.interfaces;

import by.smertex.dto.security.JwtRequest;
import by.smertex.dto.security.SecurityUserDto;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

/**
 * Сервис, обеспечивающий взаимодействие с контекстом Spring Security. А именно с аутентификацией пользователя в системе.
 * Данный сервис также обеспечивает возможность взять пользователя из контекста.
 */
public interface AuthService {

    /**
     * Возвращает JWT при успешном прохождении аутентификации
     */
    ResponseEntity<?> authentication(JwtRequest authRequest);

    Optional<SecurityUserDto> takeUserFromContext();

}
