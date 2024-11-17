package by.smertex.realisation.mapper;

import by.smertex.interfaces.mapper.Mapper;
import by.smertex.realisation.database.entity.Metainfo;
import by.smertex.realisation.dto.read.ReadMetainfoDto;
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
                .closedAt(from.getClosedAt())
                .createBy(userToReadUserDtoMapper.map(from.getCreatedBy()))
                .build();
    }
}
