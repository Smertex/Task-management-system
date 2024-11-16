package by.smertex.service;

import by.smertex.annotation.IT;
import by.smertex.database.entity.User;
import by.smertex.database.entity.enums.Status;
import by.smertex.database.repository.UserRepository;
import by.smertex.dto.filter.TaskAdminFilter;
import by.smertex.dto.read.ReadTaskDto;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IT
@RequiredArgsConstructor
public class TaskServiceIT {

    private static final UUID USER_TEST_ID = UUID.fromString("11d1b3a8-0def-4a8a-b00f-b51c43cd14e3");

    private static final String USER_EMAIL_TEST = "evgenii@gmail.com";

    private final TaskService taskService;

    private final UserRepository userRepository;


    /**
     * Поиск по фильтрации и пагинации
     */
    @Test
    void findByFilter(){
        Optional<User> optionalUser = userRepository.findById(USER_TEST_ID);

        assertTrue(optionalUser.isPresent());

        User user = optionalUser.get();

//        TaskAdminFilter taskAdminFilter = TaskAdminFilter.builder()
//                .createdBy(user)
//                .status(Status.WAITING)
//                .build();
//
//        Pageable pageable = PageRequest.of(0, 2);
//
//        List<ReadTaskDto> readTaskDtoList = taskService.findAllByFilter(taskAdminFilter, pageable);

//        assertThat(readTaskDtoList).hasSize(1);
    }


}
