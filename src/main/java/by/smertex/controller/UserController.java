package by.smertex.controller;

import by.smertex.dto.filter.TaskUserFilter;
import by.smertex.dto.read.ReadTaskDto;
import by.smertex.dto.update.CreateOrUpdateTaskUserDto;
import by.smertex.service.TaskService;
import by.smertex.util.ApiPath;
import by.smertex.util.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPath.USER_PATH)
public class UserController {

    private final TaskService taskService;

    @GetMapping
    public List<ReadTaskDto> findAllByToken(@RequestBody TaskUserFilter filter,
                                            Pageable pageable){
        return taskService.findAllByTokenAndFilter(filter, pageable);
    }

    @PutMapping("/{id}")
    public ReadTaskDto updateTask(@PathVariable UUID id,
                                  @Validated @RequestBody CreateOrUpdateTaskUserDto dto){
        return taskService.updateTask(id, dto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.UPDATE_TASK_NOT_FOUND));
    }

    @PostMapping
    public ReadTaskDto create(@Validated @RequestBody CreateOrUpdateTaskUserDto dto){
        return taskService.save(dto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ResponseMessage.CREATE_TASK_EXCEPTION));
    }
}
