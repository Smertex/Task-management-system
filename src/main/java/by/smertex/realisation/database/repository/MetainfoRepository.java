package by.smertex.realisation.database.repository;

import by.smertex.realisation.database.entity.Metainfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MetainfoRepository extends JpaRepository<Metainfo, UUID> {
}
