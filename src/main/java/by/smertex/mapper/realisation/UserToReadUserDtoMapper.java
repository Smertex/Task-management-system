package by.smertex.mapper.realisation;

import by.smertex.mapper.interfaces.Mapper;
import by.smertex.database.entity.realisation.User;
import by.smertex.dto.read.ReadUserDto;
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
