package by.smertex.repository;

import by.smertex.annotation.IT;
import by.smertex.realisation.database.entity.User;
import by.smertex.realisation.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@IT
@RequiredArgsConstructor
public class UserRepositoryIT {

    private static final String USER_EMAIL_TEST = "smertexx@gmail.com";

    private final UserRepository userRepository;

    @Test
    void findByEmail(){
        Optional<User> user = userRepository.findByEmail(USER_EMAIL_TEST);
        assertFalse(user.isEmpty());
        user.ifPresent(el -> assertEquals(el.getEmail(), USER_EMAIL_TEST));
    }

}
