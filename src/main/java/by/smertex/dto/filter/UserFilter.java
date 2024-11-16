package by.smertex.dto.filter;

import by.smertex.database.entity.enums.Role;
import jakarta.validation.constraints.Email;

public record UserFilter(@Email String email,
                         Role role) {
}
