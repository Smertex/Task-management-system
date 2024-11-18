package by.smertex.service.interfaces;

import by.smertex.dto.security.JwtRequest;
import by.smertex.dto.security.SecurityUserDto;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface AuthService {

    ResponseEntity<?> authentication(JwtRequest authRequest);

    Optional<SecurityUserDto> takeUserFromContext();

}
