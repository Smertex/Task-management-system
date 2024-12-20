package by.smertex.service.realisation;

import by.smertex.dto.exception.ApplicationResponse;
import by.smertex.dto.security.SecurityUserDto;
import by.smertex.service.interfaces.AuthService;
import by.smertex.service.interfaces.LoadUserService;
import by.smertex.database.entity.realisation.enums.Role;
import by.smertex.dto.security.JwtRequest;
import by.smertex.util.JwtTokenUtils;
import by.smertex.util.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final LoadUserService loadUserService;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtils jwtTokenUtils;

    @Override
    @CacheEvict(value = "users", key = "#authRequest.username()")
    public ResponseEntity<?> authentication(JwtRequest authRequest){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest()
                    .body(new ApplicationResponse(ResponseMessage.UNAUTHORIZED_USER, HttpStatus.UNAUTHORIZED, LocalDateTime.now()));
        }
        UserDetails userDetails = loadUserService.loadUserByUsername(authRequest.username());
        String token = jwtTokenUtils.generateToken(userDetails);
        return ResponseEntity.ok(token);
    }

    @Override
    public Optional<SecurityUserDto> takeUserFromContext(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Optional.of(new SecurityUserDto(
                (String) authentication.getPrincipal(),
                authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals(Role.ADMIN.getEditedRole()))));
    }
}
