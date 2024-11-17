package by.smertex.interfaces.service;

import by.smertex.realisation.database.entity.Metainfo;

import java.util.Optional;

public interface MetainfoService {
    Optional<Metainfo> save();
}
