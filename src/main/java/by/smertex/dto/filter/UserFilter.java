package by.smertex.dto.filter;

import by.smertex.database.entity.enums.Role;

public record UserFilter(String email,
                         Role role) {
}
