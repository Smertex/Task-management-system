package by.smertex.mapper.realisation;

import by.smertex.dto.read.ReadCommentDto;
import by.smertex.mapper.interfaces.Mapper;
import by.smertex.database.entity.realisation.Comment;
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
