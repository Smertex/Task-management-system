package by.smertex.mapper.realisation;

import by.smertex.mapper.interfaces.Mapper;
import by.smertex.database.entity.realisation.Comment;
import by.smertex.dto.update.CreateOrUpdateCommentDto;
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
