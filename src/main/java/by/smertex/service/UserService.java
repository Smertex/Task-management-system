package by.smertex.service;

import by.smertex.database.entity.User;
import by.smertex.database.repository.UserRepository;
import by.smertex.mapper.UserToReadUserDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    private final UserToReadUserDtoMapper userToReadUserDtoMapper;

    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

}
