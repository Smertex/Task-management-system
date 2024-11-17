package by.smertex.service;

import by.smertex.annotation.IT;
import by.smertex.dto.filter.CommentFilter;
import by.smertex.dto.filter.UserFilter;
import by.smertex.dto.read.ReadCommentDto;
import by.smertex.dto.security.SecurityUserDto;
import by.smertex.dto.update.CreateOrUpdateCommentDto;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@IT
@RequiredArgsConstructor
@ExtendWith(MockitoExtension.class)
public class CommentServiceIT {
    private static final String USER_EMAIL_TEST = "evgenii@gmail.com";

    private static final String ADMIN_EMAIL_TEST = "smertexx@gmail.com";

    private static final UUID TASK_ID_WHERE_PERFORMER_USER_TEST = UUID.fromString("a9099b32-e5b2-41aa-9ab6-d4d461549c70");

    private static final UUID TASK_ID_WHERE_PERFORMER_ADMIN_TEST = UUID.fromString("5f0288e7-301b-416b-af58-dd433667a607");

    private static final UUID COMMENT_ID_WHERE_CREATOR_USER_TEST = UUID.fromString("e7405417-bbad-45bf-a48a-abfe7a35f785");

    private static final Integer SECOND_FROM_CREATION = 10;

    private static final Integer PAGE_NUMBER = 0;

    private static final Integer PAGE_SIZE = 2;

    @InjectMocks
    private final CommentService commentService;

    private final TaskService taskService;

    @MockBean
    private final AuthService authService;

    @Test
    void findAllByFilterForUserWherePerformerTask(){
        Mockito.doReturn(Optional.of(new SecurityUserDto(USER_EMAIL_TEST, false)))
                .when(authService)
                .takeUserFromContext();
        CommentFilter filter = CommentFilter.builder()
                .createdBy(UserFilter.builder()
                        .build())
                .build();
        Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

        List<ReadCommentDto> readCommentDtoList = commentService.findAllByFilter(TASK_ID_WHERE_PERFORMER_USER_TEST, filter, pageable);

        assertFalse(readCommentDtoList.isEmpty());
        assertEquals(taskService.findById(TASK_ID_WHERE_PERFORMER_USER_TEST).orElseThrow()
                .getPerformer().getEmail(), USER_EMAIL_TEST);
    }

    @Test
    void findAllByFilterForUserWhereNotPerformerTask(){
        Mockito.doReturn(Optional.of(new SecurityUserDto(USER_EMAIL_TEST, false)))
                .when(authService)
                .takeUserFromContext();
        CommentFilter filter = CommentFilter.builder()
                .createdBy(UserFilter.builder()
                        .build())
                .build();
        Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

        List<ReadCommentDto> readCommentDtoListNotPerformer = commentService.findAllByFilter(TASK_ID_WHERE_PERFORMER_ADMIN_TEST, filter, pageable);
        assertTrue(readCommentDtoListNotPerformer.isEmpty());

        Mockito.doReturn(Optional.of(new SecurityUserDto(ADMIN_EMAIL_TEST, false)))
                .when(authService)
                .takeUserFromContext();
        List<ReadCommentDto> readCommentDtoListPerformer = commentService.findAllByFilter(TASK_ID_WHERE_PERFORMER_ADMIN_TEST, filter, pageable);
        assertFalse(readCommentDtoListPerformer.isEmpty());
    }

    @Test
    void findAllByFilterForAdminWhereAdminNotPerformer(){
        Mockito.doReturn(Optional.of(new SecurityUserDto(ADMIN_EMAIL_TEST, true)))
                .when(authService)
                .takeUserFromContext();
        CommentFilter filter = CommentFilter.builder()
                .createdBy(UserFilter.builder()
                        .build())
                .build();
        Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
        List<ReadCommentDto> readCommentDtoListNotPerformer = commentService.findAllByFilter(TASK_ID_WHERE_PERFORMER_USER_TEST, filter, pageable);
        assertFalse(readCommentDtoListNotPerformer.isEmpty());
    }

    @Test
    void addForUserWherePerformerTask(){
        Mockito.doReturn(Optional.of(new SecurityUserDto(USER_EMAIL_TEST, false)))
                .when(authService)
                .takeUserFromContext();
        CreateOrUpdateCommentDto createOrUpdateCommentDto = new CreateOrUpdateCommentDto("Test add comment");
        ReadCommentDto readCommentDto = commentService.add(TASK_ID_WHERE_PERFORMER_USER_TEST, createOrUpdateCommentDto)
                .orElseThrow();

        assertEquals(readCommentDto.createdBy().email(), USER_EMAIL_TEST);
        assert ChronoUnit.SECONDS.between(readCommentDto.createdAt(), LocalDateTime.now()) < SECOND_FROM_CREATION;
    }

    @Test
    void addForUserWhereNotPerformerTask(){
        Mockito.doReturn(Optional.of(new SecurityUserDto(USER_EMAIL_TEST, false)))
                .when(authService)
                .takeUserFromContext();
        CreateOrUpdateCommentDto createOrUpdateCommentDto = new CreateOrUpdateCommentDto("Test add comment");
        Optional<ReadCommentDto> readCommentDto = commentService.add(TASK_ID_WHERE_PERFORMER_ADMIN_TEST, createOrUpdateCommentDto);
        assertTrue(readCommentDto.isEmpty());
    }

    @Test
    void addForAdminWhereNotPerformerTask(){
        Mockito.doReturn(Optional.of(new SecurityUserDto(ADMIN_EMAIL_TEST, true)))
                .when(authService)
                .takeUserFromContext();
        CreateOrUpdateCommentDto createOrUpdateCommentDto = new CreateOrUpdateCommentDto("Test add comment");
        Optional<ReadCommentDto> readCommentDto = commentService.add(TASK_ID_WHERE_PERFORMER_ADMIN_TEST, createOrUpdateCommentDto);
        assertTrue(readCommentDto.isPresent());
        assertNotEquals(taskService.findById(TASK_ID_WHERE_PERFORMER_USER_TEST).orElseThrow()
                .getPerformer().getEmail(), ADMIN_EMAIL_TEST);
    }

    @Test
    void updateCommentForCreator(){
        Mockito.doReturn(Optional.of(new SecurityUserDto(USER_EMAIL_TEST, false)))
                .when(authService)
                .takeUserFromContext();
        CreateOrUpdateCommentDto createOrUpdateCommentDto = new CreateOrUpdateCommentDto("Test update comment");
        Optional<ReadCommentDto> readCommentDto = commentService.update(COMMENT_ID_WHERE_CREATOR_USER_TEST, createOrUpdateCommentDto);
        assertFalse(readCommentDto.isEmpty());
    }

    @Test
    void updateCommentNotForCreator(){
        Mockito.doReturn(Optional.of(new SecurityUserDto(ADMIN_EMAIL_TEST, true)))
                .when(authService)
                .takeUserFromContext();
        CreateOrUpdateCommentDto createOrUpdateCommentDto = new CreateOrUpdateCommentDto("Test update comment");
        Optional<ReadCommentDto> readCommentDto = commentService.update(COMMENT_ID_WHERE_CREATOR_USER_TEST, createOrUpdateCommentDto);
        assertTrue(readCommentDto.isEmpty());
    }
}
