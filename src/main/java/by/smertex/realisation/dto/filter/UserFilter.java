package by.smertex.realisation.dto.filter;

import by.smertex.realisation.database.entity.enums.Role;
import jakarta.validation.constraints.Email;
import lombok.Builder;

@Builder
public record UserFilter(@Email String email,
                         Role role) {
}
