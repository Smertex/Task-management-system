package by.smertex.service;

import by.smertex.annotation.IT;
import by.smertex.dto.filter.CommentFilter;
import by.smertex.dto.filter.UserFilter;
import by.smertex.dto.read.ReadCommentDto;
import by.smertex.dto.security.SecurityUserDto;
import by.smertex.dto.update.CreateOrUpdateCommentDto;
import by.smertex.service.realisation.AuthServiceImpl;
import by.smertex.service.realisation.CommentServiceImpl;
import by.smertex.service.realisation.TaskServiceImpl;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@IT
@RequiredArgsConstructor
@ExtendWith(MockitoExtension.class)
public class CommentServiceImplIT {
    private static final String USER_EMAIL_TEST = "evgenii@gmail.com";

    private static final String ADMIN_EMAIL_TEST = "smertexx@gmail.com";

    private static final UUID TASK_ID_WHERE_PERFORMER_USER_TEST = UUID.fromString("a9099b32-e5b2-41aa-9ab6-d4d461549c70");

    private static final UUID TASK_ID_WHERE_PERFORMER_ADMIN_TEST = UUID.fromString("5f0288e7-301b-416b-af58-dd433667a607");

    private static final UUID COMMENT_ID_WHERE_CREATOR_USER_TEST = UUID.fromString("5e0dc76a-f6d1-44b9-9f6a-259915ceef7c");

    private static final Integer SECOND_FROM_CREATION = 10;

    private static final Integer PAGE_NUMBER = 0;

    private static final Integer PAGE_SIZE = 2;

    @InjectMocks
    private final CommentServiceImpl commentServiceImpl;

    private final TaskServiceImpl taskServiceImpl;

    @MockBean
    private final AuthServiceImpl authServiceImpl;

    @Test
    void findAllByFilterForUserWherePerformerTask(){
        Mockito.doReturn(Optional.of(new SecurityUserDto(USER_EMAIL_TEST, false)))
                .when(authServiceImpl)
                .takeUserFromContext();
        CommentFilter filter = CommentFilter.builder()
                .createdBy(UserFilter.builder()
                        .build())
                .build();
        Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

        Page<ReadCommentDto> readCommentDtoList = commentServiceImpl.findAllByFilter(TASK_ID_WHERE_PERFORMER_USER_TEST, filter, pageable);

        assertFalse(readCommentDtoList.isEmpty());
        assertEquals(taskServiceImpl.findById(TASK_ID_WHERE_PERFORMER_USER_TEST).orElseThrow()
                .getPerformer().getEmail(), USER_EMAIL_TEST);
    }

    @Test
    void findAllByFilterForUserWhereNotPerformerTask(){
        Mockito.doReturn(Optional.of(new SecurityUserDto(USER_EMAIL_TEST, false)))
                .when(authServiceImpl)
                .takeUserFromContext();
        CommentFilter filter = CommentFilter.builder()
                .createdBy(UserFilter.builder()
                        .build())
                .build();
        Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

        Page<ReadCommentDto> readCommentDtoListNotPerformer = commentServiceImpl.findAllByFilter(TASK_ID_WHERE_PERFORMER_ADMIN_TEST, filter, pageable);
        assertTrue(readCommentDtoListNotPerformer.isEmpty());

        Mockito.doReturn(Optional.of(new SecurityUserDto(ADMIN_EMAIL_TEST, false)))
                .when(authServiceImpl)
                .takeUserFromContext();
        Page<ReadCommentDto> readCommentDtoListPerformer = commentServiceImpl.findAllByFilter(TASK_ID_WHERE_PERFORMER_ADMIN_TEST, filter, pageable);
        assertFalse(readCommentDtoListPerformer.isEmpty());
    }

    @Test
    void findAllByFilterForAdminWhereAdminNotPerformer(){
        Mockito.doReturn(Optional.of(new SecurityUserDto(ADMIN_EMAIL_TEST, true)))
                .when(authServiceImpl)
                .takeUserFromContext();
        CommentFilter filter = CommentFilter.builder()
                .createdBy(UserFilter.builder()
                        .build())
                .build();
        Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
        Page<ReadCommentDto> readCommentDtoListNotPerformer = commentServiceImpl.findAllByFilter(TASK_ID_WHERE_PERFORMER_USER_TEST, filter, pageable);
        assertFalse(readCommentDtoListNotPerformer.isEmpty());
    }

    @Test
    void addForUserWherePerformerTask(){
        Mockito.doReturn(Optional.of(new SecurityUserDto(USER_EMAIL_TEST, false)))
                .when(authServiceImpl)
                .takeUserFromContext();
        CreateOrUpdateCommentDto createOrUpdateCommentDto = new CreateOrUpdateCommentDto("Test add comment");
        ReadCommentDto readCommentDto = commentServiceImpl.add(TASK_ID_WHERE_PERFORMER_USER_TEST, createOrUpdateCommentDto)
                .orElseThrow();

        assertEquals(readCommentDto.createdBy().email(), USER_EMAIL_TEST);
        assert ChronoUnit.SECONDS.between(readCommentDto.createdAt(), LocalDateTime.now()) < SECOND_FROM_CREATION;
    }

    @Test
    void addForUserWhereNotPerformerTask(){
        Mockito.doReturn(Optional.of(new SecurityUserDto(USER_EMAIL_TEST, false)))
                .when(authServiceImpl)
                .takeUserFromContext();
        CreateOrUpdateCommentDto createOrUpdateCommentDto = new CreateOrUpdateCommentDto("Test add comment");
        Optional<ReadCommentDto> readCommentDto = commentServiceImpl.add(TASK_ID_WHERE_PERFORMER_ADMIN_TEST, createOrUpdateCommentDto);
        assertTrue(readCommentDto.isEmpty());
    }

    @Test
    void addForAdminWhereNotPerformerTask(){
        Mockito.doReturn(Optional.of(new SecurityUserDto(ADMIN_EMAIL_TEST, true)))
                .when(authServiceImpl)
                .takeUserFromContext();
        CreateOrUpdateCommentDto createOrUpdateCommentDto = new CreateOrUpdateCommentDto("Test add comment");
        Optional<ReadCommentDto> readCommentDto = commentServiceImpl.add(TASK_ID_WHERE_PERFORMER_ADMIN_TEST, createOrUpdateCommentDto);
        assertTrue(readCommentDto.isPresent());
        assertNotEquals(taskServiceImpl.findById(TASK_ID_WHERE_PERFORMER_USER_TEST).orElseThrow()
                .getPerformer().getEmail(), ADMIN_EMAIL_TEST);
    }

    @Test
    void updateCommentForCreator(){
        Mockito.doReturn(Optional.of(new SecurityUserDto(USER_EMAIL_TEST, false)))
                .when(authServiceImpl)
                .takeUserFromContext();
        CreateOrUpdateCommentDto createOrUpdateCommentDto = new CreateOrUpdateCommentDto("Test update comment");
        Optional<ReadCommentDto> readCommentDto = commentServiceImpl.update(COMMENT_ID_WHERE_CREATOR_USER_TEST, createOrUpdateCommentDto);
        assertFalse(readCommentDto.isEmpty());
    }

    @Test
    void updateCommentNotForCreator(){
        Mockito.doReturn(Optional.of(new SecurityUserDto(ADMIN_EMAIL_TEST, true)))
                .when(authServiceImpl)
                .takeUserFromContext();
        CreateOrUpdateCommentDto createOrUpdateCommentDto = new CreateOrUpdateCommentDto("Test update comment");
        Optional<ReadCommentDto> readCommentDto = commentServiceImpl.update(COMMENT_ID_WHERE_CREATOR_USER_TEST, createOrUpdateCommentDto);
        assertTrue(readCommentDto.isEmpty());
    }
}
