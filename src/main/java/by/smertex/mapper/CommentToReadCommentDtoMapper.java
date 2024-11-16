package by.smertex.mapper;

import by.smertex.database.entity.Comment;
import by.smertex.dto.read.ReadCommentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentToReadCommentDtoMapper implements Mapper<Comment, ReadCommentDto>{

    private final UserToReadUserDtoMapper userToReadUserDtoMapper;

    @Override
    public ReadCommentDto map(Comment from) {
        return ReadCommentDto.builder()
                .content(from.getContent())
                .createdAt(from.getCreatedAt())
                .createdBy(userToReadUserDtoMapper.map(from.getCreatedBy()))
                .build();
    }
}
