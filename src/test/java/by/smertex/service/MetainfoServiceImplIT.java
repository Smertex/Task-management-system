package by.smertex.service;

import by.smertex.annotation.IT;
import by.smertex.realisation.database.entity.Metainfo;
import by.smertex.realisation.dto.security.SecurityUserDto;
import by.smertex.realisation.service.AuthServiceImpl;
import by.smertex.realisation.service.MetainfoService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@IT
@RequiredArgsConstructor
@ExtendWith(MockitoExtension.class)
public class MetainfoServiceImplIT {

    private static final String ADMIN_EMAIL_TEST = "smertexx@gmail.com";

    private static final Integer SECOND_FROM_CREATION = 10;

    @InjectMocks
    private final MetainfoService metainfoServiceImpl;

    @MockBean
    private final AuthServiceImpl authServiceImpl;

    @Test
    void save() {
        Mockito.doReturn(Optional.of(new SecurityUserDto(ADMIN_EMAIL_TEST, true)))
                .when(authServiceImpl)
                .takeUserFromContext();
        Optional<Metainfo> metainfo = metainfoServiceImpl.save();
        metainfo.ifPresent(el -> assertEquals(el.getCreatedBy().getEmail(), ADMIN_EMAIL_TEST));
        assert ChronoUnit.SECONDS.between(metainfo.orElseThrow().getCreatedAt(), LocalDateTime.now()) < SECOND_FROM_CREATION;
    }

}
