package by.smertex.database.repository.interfaces;

import by.smertex.database.entity.realisation.Metainfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MetainfoRepository extends JpaRepository<Metainfo, UUID> {
}