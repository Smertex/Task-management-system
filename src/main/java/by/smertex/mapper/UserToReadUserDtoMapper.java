package by.smertex.mapper;

import by.smertex.database.entity.User;
import by.smertex.dto.read.ReadUserDto;
import org.springframework.stereotype.Component;

@Component
public class UserToReadUserDtoMapper implements Mapper<User, ReadUserDto>{
    @Override
    public ReadUserDto map(User from) {
        return ReadUserDto.builder()
                .id(from.getId())
                .email(from.getEmail())
                .role(from.getRole())
                .build();
    }
}
