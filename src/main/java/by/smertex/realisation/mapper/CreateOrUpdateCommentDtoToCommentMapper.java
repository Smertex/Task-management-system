package by.smertex.realisation.mapper;

import by.smertex.interfaces.mapper.Mapper;
import by.smertex.realisation.database.entity.Comment;
import by.smertex.realisation.dto.update.CreateOrUpdateCommentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateOrUpdateCommentDtoToCommentMapper implements Mapper<CreateOrUpdateCommentDto, Comment> {

    @Override
    public Comment map(CreateOrUpdateCommentDto from) {
        return copy(from, new Comment());
    }

    @Override
    public Comment map(CreateOrUpdateCommentDto from, Comment to) {
        return copy(from, to);
    }

    private Comment copy(CreateOrUpdateCommentDto from, Comment to){
        to.setContent(from.content());
        return to;
    }
}
