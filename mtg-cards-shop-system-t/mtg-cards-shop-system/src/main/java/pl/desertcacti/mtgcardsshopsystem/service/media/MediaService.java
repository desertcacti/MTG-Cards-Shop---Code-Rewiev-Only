package pl.desertcacti.mtgcardsshopsystem.service.media;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

/** MediaService interface provides method declarations for media-related operations such as retrieving images by name. */
public interface MediaService {
    ResponseEntity<Resource> getImageByName(String pictureName);
}