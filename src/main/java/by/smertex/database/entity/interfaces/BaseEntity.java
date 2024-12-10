package by.smertex.database.entity.interfaces;

import jakarta.persistence.MappedSuperclass;

import java.io.Serializable;
import java.util.UUID;

/**
 * Базовая сущность в рамках проекта, необходима для абстрагирования
 */
@MappedSuperclass
public interface BaseEntity <T extends Serializable> {
    T getId();
    void setId(UUID id);
}
