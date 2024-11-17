package by.smertex.controller;

import by.smertex.dto.filter.CommentFilter;
import by.smertex.dto.filter.TaskFilter;
import by.smertex.dto.read.ReadCommentDto;
import by.smertex.dto.read.ReadTaskDto;
import by.smertex.dto.update.CreateOrUpdateCommentDto;
import by.smertex.dto.update.CreateOrUpdateTaskDto;
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

    @GetMapping(ApiPath.TASK_PATH)
    public List<ReadTaskDto> findAll(@RequestBody @Validated TaskFilter filter,
                                                             Pageable pageable){
        return taskService.findAllByFilter(filter, pageable);
    }

    @PostMapping(ApiPath.TASK_PATH)
    public ReadTaskDto create(@Validated @RequestBody CreateOrUpdateTaskDto dto){
        return taskService.save(dto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ResponseMessage.CREATE_TASK_EXCEPTION));
    }

    @PutMapping(ApiPath.ID_TASK_PATH)
    public ReadTaskDto updateTask(@PathVariable UUID id,
                                  @Validated @RequestBody CreateOrUpdateTaskDto dto){
        return taskService.update(id, dto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.UPDATE_TASK_NOT_FOUND));
    }

    @GetMapping(ApiPath.COMMENT_IN_TASK_PATH)
    public List<ReadCommentDto> findAllComment(@PathVariable UUID id,
                                               @Validated @RequestBody CommentFilter filter,
                                         Pageable pageable){
        return commentService.findAllByFilter(id, filter, pageable);
    }

    @PostMapping(ApiPath.COMMENT_IN_TASK_PATH)
    public ReadCommentDto addComment(@PathVariable UUID id,
                                     @Validated @RequestBody CreateOrUpdateCommentDto dto){
        return commentService.add(id, dto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ResponseMessage.ADD_COMMENT_EXCEPTION));
    }

    @PutMapping(ApiPath.COMMENT_UPDATE_PATH)
    public ReadCommentDto updateComment(@PathVariable UUID id,
                                        @Validated @RequestBody CreateOrUpdateCommentDto dto){
        return commentService.update(id, dto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ResponseMessage.UPDATE_COMMENT_EXCEPTION));
    }
}
