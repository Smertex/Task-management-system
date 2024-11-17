package by.smertex.interfaces.entity;

import jakarta.persistence.MappedSuperclass;

import java.io.Serializable;
import java.util.UUID;

@MappedSuperclass
public interface BaseEntity <T extends Serializable> {
    T getId();
    void setId(UUID id);
}
