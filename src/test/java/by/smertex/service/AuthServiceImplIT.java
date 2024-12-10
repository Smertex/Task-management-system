package by.smertex.service;

import by.smertex.annotation.IT;
import by.smertex.database.entity.realisation.enums.Role;
import by.smertex.dto.security.JwtRequest;
import by.smertex.service.realisation.AuthServiceImpl;
import by.smertex.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IT
@RequiredArgsConstructor
public class AuthServiceImplIT {

    private static final String USER_EMAIL_TEST = "evgenii@gmail.com";

    private static final String ADMIN_EMAIL_TEST = "smertexx@gmail.com";

    private static final String PASSWORD_TEST = "qwertyui12345678";

    private final AuthServiceImpl authServiceImpl;

    private final JwtTokenUtils jwtTokenUtils;

    @Test
    @SuppressWarnings("all")
    void authenticationAdmin(){
        JwtRequest jwtRequest = JwtRequest.builder()
                .username(ADMIN_EMAIL_TEST)
                .password(PASSWORD_TEST)
                .build();

        ResponseEntity<?> response = authServiceImpl.authentication(jwtRequest);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        String token = (String) response.getBody();

        assertEquals(jwtTokenUtils.getUsername(token), ADMIN_EMAIL_TEST);
        jwtTokenUtils.getRoles(token).contains(Role.ADMIN.getEditedRole());
    }

    @Test
    @SuppressWarnings("all")
    void authenticationUser(){
        JwtRequest jwtRequest = JwtRequest.builder()
                .username(USER_EMAIL_TEST)
                .password(PASSWORD_TEST)
                .build();

        ResponseEntity<?> response = authServiceImpl.authentication(jwtRequest);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        String token = (String) response.getBody();

        assertEquals(jwtTokenUtils.getUsername(token), USER_EMAIL_TEST);
        jwtTokenUtils.getRoles(token).contains(Role.USER.getEditedRole());
    }

    @Test
    void incorrectPasswordOrLoginTest(){
        JwtRequest jwtRequest = JwtRequest.builder()
                .username(ADMIN_EMAIL_TEST + ".")
                .password(PASSWORD_TEST)
                .build();
        ResponseEntity<?> response = authServiceImpl.authentication(jwtRequest);
        assertTrue(response.getStatusCode().is4xxClientError());
    }

}
