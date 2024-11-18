package by.smertex.database.entity.interfaces;

import jakarta.persistence.MappedSuperclass;

import java.io.Serializable;
import java.util.UUID;

@MappedSuperclass
public interface BaseEntity <T extends Serializable> {
    T getId();
    void setId(UUID id);
}
