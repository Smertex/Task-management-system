package by.smertex.service.realisation;

import by.smertex.dto.security.SecurityUserDto;
import by.smertex.service.interfaces.AuthService;
import by.smertex.service.interfaces.UserService;
import by.smertex.controller.exception.UserNotFoundInDatabaseException;
import by.smertex.database.entity.realisation.Metainfo;
import by.smertex.database.repository.interfaces.MetainfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MetainfoService implements by.smertex.service.interfaces.MetainfoService {

    private final UserService userService;

    private final AuthService authService;

    private final MetainfoRepository metainfoRepository;

    @Override
    @Transactional
    public Optional<Metainfo> save(){
        SecurityUserDto securityUserDto = authService.takeUserFromContext()
                .orElseThrow();
        return Optional.of(Metainfo.builder()
                        .createdBy(userService.findByEmail(securityUserDto.email())
                                .orElseThrow(() -> new UserNotFoundInDatabaseException(securityUserDto.email())))
                        .createdAt(LocalDateTime.now())
                .build()).map(metainfoRepository::save);
    }

}
