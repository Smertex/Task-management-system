package by.smertex.realisation.mapper;

import by.smertex.interfaces.mapper.Mapper;
import by.smertex.realisation.database.entity.Comment;
import by.smertex.realisation.dto.read.ReadCommentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentToReadCommentDtoMapper implements Mapper<Comment, ReadCommentDto> {

    private final UserToReadUserDtoMapper userToReadUserDtoMapper;

    @Override
    public ReadCommentDto map(Comment from) {
        return ReadCommentDto.builder()
                .id(from.getId())
                .content(from.getContent())
                .createdAt(from.getCreatedAt())
                .createdBy(userToReadUserDtoMapper.map(from.getCreatedBy()))
                .build();
    }
}
