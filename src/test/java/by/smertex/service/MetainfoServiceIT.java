package by.smertex.service;

import by.smertex.annotation.IT;
import by.smertex.database.entity.Metainfo;
import by.smertex.dto.security.SecurityUserDto;
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
public class MetainfoServiceIT {

    private static final String ADMIN_EMAIL_TEST = "smertexx@gmail.com";

    private static final Integer SECOND_FROM_CREATION = 10;

    @InjectMocks
    private final MetainfoService metainfoService;

    @MockBean
    private final AuthService authService;

    @Test
    void save() {
        Mockito.doReturn(Optional.of(new SecurityUserDto(ADMIN_EMAIL_TEST, true)))
                .when(authService)
                .takeUserFromContext();
        Optional<Metainfo> metainfo = metainfoService.save();
        metainfo.ifPresent(el -> assertEquals(el.getCreatedBy().getEmail(), ADMIN_EMAIL_TEST));
        assert ChronoUnit.SECONDS.between(metainfo.orElseThrow().getCreatedAt(), LocalDateTime.now()) < SECOND_FROM_CREATION;
    }

}
