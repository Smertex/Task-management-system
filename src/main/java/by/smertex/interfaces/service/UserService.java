package by.smertex.interfaces.service;

import by.smertex.realisation.database.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByEmail(String email);
}
