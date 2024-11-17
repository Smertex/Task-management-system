package by.smertex.service;

import by.smertex.annotation.IT;
import by.smertex.database.entity.User;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@IT
@RequiredArgsConstructor
public class UserServiceIT {

    private static final String ADMIN_EMAIL_TEST = "smertexx@gmail.com";

    private final UserService userService;

    @Test
    void findUserByEmail(){
        Optional<User> user = userService.findByEmail(ADMIN_EMAIL_TEST);
        assertFalse(user.isEmpty());
        user.ifPresent(el -> assertEquals(el.getEmail(), ADMIN_EMAIL_TEST));
    }

}
