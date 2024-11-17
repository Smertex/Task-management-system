package by.smertex.realisation.database.entity.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER,
    ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }

    public String getEditedRole(){
        return "ROLE_" + name();
    }
}
