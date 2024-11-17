package by.smertex.realisation.service;

import by.smertex.interfaces.service.AuthService;
import by.smertex.interfaces.service.UserService;
import by.smertex.realisation.database.entity.Metainfo;
import by.smertex.realisation.database.repository.MetainfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MetainfoService implements by.smertex.interfaces.service.MetainfoService {

    private final UserService userService;

    private final AuthService authService;

    private final MetainfoRepository metainfoRepository;

    @Override
    @Transactional
    public Optional<Metainfo> save(){
        return Optional.of(Metainfo.builder()
                        .createdBy(userService.findByEmail(authService.takeUserFromContext().
                                        orElseThrow().email())
                                .orElseThrow())
                        .createdAt(LocalDateTime.now())
                .build()).map(metainfoRepository::save);
    }

}
