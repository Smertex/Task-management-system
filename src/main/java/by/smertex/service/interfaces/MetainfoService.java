package by.smertex.service.interfaces;

import by.smertex.database.entity.realisation.Metainfo;

import java.util.Optional;

/**
 * Сервис для работы с метаинформацией. Обеспечивает ее создание и сохранения в рамках БД
 */
public interface MetainfoService {
    Optional<Metainfo> save();
}
