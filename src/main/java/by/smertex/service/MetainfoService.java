package by.smertex.service;

import by.smertex.database.entity.Metainfo;
import by.smertex.database.repository.MetainfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MetainfoService {

    private final UserService userService;

    private final MetainfoRepository metainfoRepository;

    @Transactional
    public Optional<Metainfo> save(){
        return Optional.of(Metainfo.builder()
                        .createdBy(userService.findByEmail((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                                .orElseThrow())
                        .createdAt(LocalDateTime.now())
                .build()).map(metainfoRepository::save);
    }

}
