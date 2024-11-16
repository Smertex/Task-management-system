package by.smertex.controller;

import by.smertex.dto.filter.CommentFilter;
import by.smertex.dto.filter.TaskFilter;
import by.smertex.dto.read.ReadCommentDto;
import by.smertex.dto.read.ReadTaskDto;
import by.smertex.dto.update.CreateOrUpdateUserDto;
import by.smertex.service.CommentService;
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

    private final CommentService commentService;

    @GetMapping
    public List<ReadTaskDto> findAllByToken(@RequestBody TaskFilter filter,
                                            Pageable pageable){
        return taskService.findAllByFilter(filter, pageable);
    }

    @GetMapping(ApiPath.COMMENT_PATH)
    public List<ReadCommentDto> findAllComment(@PathVariable UUID id,
                                         @Validated @RequestBody CommentFilter filter,
                                         Pageable pageable){
        return commentService.findAllByFilter(id, filter, pageable);
    }

    @PutMapping(ApiPath.ID_PATH)
    public ReadTaskDto updateTask(@PathVariable UUID id,
                                  @Validated @RequestBody CreateOrUpdateUserDto dto){
        return taskService.update(id, dto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.UPDATE_TASK_NOT_FOUND));
    }

    @PostMapping
    public ReadTaskDto create(@Validated @RequestBody CreateOrUpdateUserDto dto){
        return taskService.save(dto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ResponseMessage.CREATE_TASK_EXCEPTION));
    }
}
