package by.smertex.realisation.service;

import by.smertex.interfaces.service.AuthService;
import by.smertex.interfaces.service.LoadUserService;
import by.smertex.realisation.database.entity.enums.Role;
import by.smertex.realisation.dto.exception.ApplicationResponse;
import by.smertex.realisation.dto.security.JwtRequest;
import by.smertex.realisation.dto.security.SecurityUserDto;
import by.smertex.util.JwtTokenUtils;
import by.smertex.util.ResponseMessage;
import lombok.RequiredArgsConstructor;
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
