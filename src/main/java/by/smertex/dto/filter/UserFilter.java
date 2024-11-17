package by.smertex.dto.filter;

import by.smertex.database.entity.enums.Role;
import jakarta.validation.constraints.Email;
import lombok.Builder;

@Builder
public record UserFilter(@Email String email,
                         Role role) {
}
