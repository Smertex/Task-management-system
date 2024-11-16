package by.smertex.mapper;

import by.smertex.database.entity.Metainfo;
import by.smertex.dto.read.ReadMetainfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MetainfoToReadMetainfoDtoMapper implements Mapper<Metainfo, ReadMetainfoDto> {

    private final UserToReadUserDtoMapper userToReadUserDtoMapper;

    @Override
    public ReadMetainfoDto map(Metainfo from) {
        return ReadMetainfoDto.builder()
                .id(from.getId())
                .createAt(from.getCreatedAt())
                .closedAt(from.getCloseAt())
                .createBy(userToReadUserDtoMapper.map(from.getCreatedBy()))
                .build();
    }
}
