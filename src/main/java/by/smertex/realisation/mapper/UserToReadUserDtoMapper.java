package by.smertex.realisation.mapper;

import by.smertex.interfaces.mapper.Mapper;
import by.smertex.realisation.database.entity.User;
import by.smertex.realisation.dto.read.ReadUserDto;
import org.springframework.stereotype.Component;

@Component
public class UserToReadUserDtoMapper implements Mapper<User, ReadUserDto> {
    @Override
    public ReadUserDto map(User from) {
        return ReadUserDto.builder()
                .id(from.getId())
                .email(from.getEmail())
                .role(from.getRole())
                .build();
    }
}
