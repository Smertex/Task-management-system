package by.smertex.interfaces.service;

import by.smertex.realisation.dto.security.JwtRequest;
import by.smertex.realisation.dto.security.SecurityUserDto;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface AuthService {

    ResponseEntity<?> authentication(JwtRequest authRequest);

    Optional<SecurityUserDto> takeUserFromContext();

}
