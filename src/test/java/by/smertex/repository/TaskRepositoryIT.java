package by.smertex.repository;

import by.smertex.annotation.IT;
import by.smertex.database.entity.QTask;
import by.smertex.database.entity.Task;
import by.smertex.database.entity.User;
import by.smertex.database.entity.enums.Priority;
import by.smertex.database.entity.enums.Status;
import by.smertex.database.repository.TaskRepository;
import by.smertex.database.repository.UserRepository;
import by.smertex.database.repository.filter.QPredicateImpl;
import by.smertex.dto.TaskFilter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@IT
@RequiredArgsConstructor
public class TaskRepositoryIT {
    private static final UUID USER_TEST_ID = UUID.fromString("11d1b3a8-0def-4a8a-b00f-b51c43cd14e3");

    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    /**
     * Поиск задач по автору с учетом пагинации
     */
    @Test
    @SuppressWarnings("all")
    void findAllByAuthor(){
        Optional<User> optionalUser = userRepository.findById(USER_TEST_ID);

        assertTrue(optionalUser.isPresent());

        User user = optionalUser.get();

        TaskFilter taskFilter = TaskFilter.builder()
                .createdBy(user)
                .build();
        List<Task> tasks = taskRepository.findAll(QPredicateImpl.builder()
                .add(taskFilter.createdBy(), QTask.task.metaInfo.createdBy::eq)
                .buildAnd(), PageRequest.of(0, 2)).getContent();

        assertThat(tasks).hasSize(2);
        tasks.forEach(task -> assertEquals(task.getMetaInfo().getCreatedBy(), user));
    }

    /**
     * Поиск задач по исполнителю с учетом пагинации
     */
    @Test
    @SuppressWarnings("all")
    void findAllByPerformer(){
        Optional<User> optionalUser = userRepository.findById(USER_TEST_ID);

        assertTrue(optionalUser.isPresent());

        User user = optionalUser.get();

        TaskFilter taskFilter = TaskFilter.builder()
                .performer(user)
                .build();
        List<Task> tasks = taskRepository.findAll(QPredicateImpl.builder()
                .add(taskFilter.performer(), QTask.task.performer::eq)
                .buildAnd(), PageRequest.of(0, 2)).getContent();

        assertThat(tasks).hasSize(1);
        tasks.forEach(task -> assertEquals(task.getPerformer(), user));
    }

    /**
     * Поиск комментариев задачи по автору с учетом пагинации
     */
    @Test
    @SuppressWarnings("all")
    void findAllCommentFromTaskByPerformer(){
        Optional<User> optionalUser = userRepository.findById(USER_TEST_ID);

        assertTrue(optionalUser.isPresent());

        User user = optionalUser.get();

        TaskFilter taskFilter = TaskFilter.builder()
                .createdBy(user)
                .build();
        List<Task> tasks = taskRepository.findAll(QPredicateImpl.builder()
                .add(taskFilter.createdBy(), QTask.task.metaInfo.createdBy::eq)
                .buildAnd(), PageRequest.of(0, 2))
                .getContent();

        assertThat(tasks).hasSize(2);

        assertEquals(tasks.get(0).getComments().size(), 0);
        assertEquals(tasks.get(1).getComments().size(), 2);
    }

    /**
     * Поиск по нескольким параметрам
     */
    @Test
    @SuppressWarnings("all")
    void findAllByFilter(){
        Optional<User> optionalUser = userRepository.findById(USER_TEST_ID);

        assertTrue(optionalUser.isPresent());

        User user = optionalUser.get();

        TaskFilter taskFilter = TaskFilter.builder()
                .createdBy(user)
                .status(Status.WAITING)
                .priority(Priority.LOWEST)
                .build();
        List<Task> tasks = taskRepository.findAll(QPredicateImpl.builder()
                .add(taskFilter.createdBy(), QTask.task.metaInfo.createdBy::eq)
                .add(taskFilter.status(), QTask.task.status::eq)
                .add(taskFilter.priority(), QTask.task.priority::eq)
                .buildAnd(), PageRequest.of(0, 2))
                .getContent();

        assertThat(tasks).hasSize(1);

        Task task = tasks.get(0);

        assertEquals(task.getMetaInfo().getCreatedBy(), taskFilter.createdBy());
        assertEquals(task.getStatus(), taskFilter.status());
        assertEquals(task.getPriority(), taskFilter.priority());
    }
}
