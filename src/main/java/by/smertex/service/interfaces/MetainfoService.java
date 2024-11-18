package by.smertex.service.interfaces;

import by.smertex.database.entity.realisation.Metainfo;

import java.util.Optional;

public interface MetainfoService {
    Optional<Metainfo> save();
}
