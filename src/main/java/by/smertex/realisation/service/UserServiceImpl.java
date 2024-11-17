package by.smertex.realisation.service;

import by.smertex.interfaces.service.UserService;
import by.smertex.realisation.database.entity.User;
import by.smertex.realisation.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

}
