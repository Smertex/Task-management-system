package by.smertex.service.interfaces;

import by.smertex.database.entity.realisation.User;

import java.util.Optional;

/**
 * Сервис для работы с пользователем
 */
public interface UserService {
    Optional<User> findByEmail(String email);
}
