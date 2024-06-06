package pl.desertcacti.mtgcardsshopsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.desertcacti.mtgcardsshopsystem.entity.MediaEntity;

import java.util.Optional;

/** MediaRepository interface provides methods for performing CRUD operations
 * on MediaEntity objects, including finding a media entity by its file name. */
public interface MediaRepository extends JpaRepository<MediaEntity, Long> {

    Optional<MediaEntity> findByFileName(String fileName);
}
