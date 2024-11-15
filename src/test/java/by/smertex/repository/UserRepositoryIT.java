package by.smertex.repository;

import by.smertex.annotation.IT;
import by.smertex.database.entity.User;
import by.smertex.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@IT
@RequiredArgsConstructor
public class UserRepositoryIT {
    private final UserRepository userRepository;

    /**
     * Проверка корреткности работы маппинга User
     */
    @Test
    void findAll(){
        List<User> users = userRepository.findAll();
        assertThat(users).hasSize(2);
    }
}
