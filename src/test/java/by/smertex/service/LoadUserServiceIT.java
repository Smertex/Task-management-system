package by.smertex.service;

import by.smertex.annotation.IT;
import by.smertex.database.entity.enums.Role;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@IT
@RequiredArgsConstructor
public class LoadUserServiceIT {

    private static final String USER_EMAIL_TEST = "evgenii@gmail.com";

    private static final String ADMIN_EMAIL_TEST = "smertexx@gmail.com";

    private static final String PASSWORD_TEST = "qwertyui12345678";

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final LoadUserService loadUserService;

    @Test
    void loadUserByUsernameForAdmin(){
        UserDetails userDetails = loadUserService.loadUserByUsername(ADMIN_EMAIL_TEST);
        assertEquals(ADMIN_EMAIL_TEST, userDetails.getUsername());
        assertTrue(bCryptPasswordEncoder.matches(PASSWORD_TEST, userDetails.getPassword()));
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals(Role.ADMIN.getEditedRole())));
    }

    @Test
    void loadUserByUsernameForUser(){
        UserDetails userDetails = loadUserService.loadUserByUsername(USER_EMAIL_TEST);
        assertEquals(USER_EMAIL_TEST, userDetails.getUsername());
        assertTrue(bCryptPasswordEncoder.matches(PASSWORD_TEST, userDetails.getPassword()));
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals(Role.USER.getEditedRole())));
    }

    @Test
    void loadUserException(){
        assertThrows(UsernameNotFoundException.class,
                () -> loadUserService.loadUserByUsername(USER_EMAIL_TEST + "."));
    }
}
