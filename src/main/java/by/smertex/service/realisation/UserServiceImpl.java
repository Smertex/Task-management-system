package by.smertex.service.realisation;

import by.smertex.service.interfaces.UserService;
import by.smertex.database.entity.realisation.User;
import by.smertex.database.repository.interfaces.UserRepository;
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
