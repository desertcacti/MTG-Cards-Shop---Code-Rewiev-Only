package pl.desertcacti.mtgcardsshopsystem.service.media;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import lombok.extern.slf4j.Slf4j;
import pl.desertcacti.mtgcardsshopsystem.entity.MediaEntity;
import pl.desertcacti.mtgcardsshopsystem.exception.MediaExceptionController;
import pl.desertcacti.mtgcardsshopsystem.repository.MediaRepository;
import java.util.Optional;

/** MediaServiceImpl class provides the implementation for media-related operations
 such as retrieving images by name using Google Drive API. */
@Service
@Slf4j
public class MediaServiceImpl implements MediaService {
    private final MediaRepository mediaRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public MediaServiceImpl(MediaRepository mediaRepository, RestTemplateBuilder restTemplateBuilder) {
        this.mediaRepository = mediaRepository;
        this.restTemplate = restTemplateBuilder.build();
    }

    /** getImageByName()
     /*  Method road map:
     *1* Retrieve MediaEntity by fileName.
     *2* Validate and get fileId from MediaEntity.
     *3* Generate image URL using fileId.
     *4* Fetch the image from the generated URL.
     *5* Create HTTP headers for the response.
     *6* Return the image as a ResponseEntity with headers and status. */
    @Override
    public ResponseEntity<Resource> getImageByName(String fileName) {
        try {
            // *1*
            MediaEntity mediaEntity = mediaRepository.findByFileName(fileName)
                    .orElseThrow(() -> new MediaExceptionController.MediaNotFoundException(fileName));
            // *2*
            String fileId = Optional.ofNullable(mediaEntity.getFileId())
                    .filter(id -> !id.isEmpty())
                    .orElseThrow(() -> new MediaExceptionController.InvalidFileIdException(fileName));
            // *3*
            String imageUrl = generateImageUrl(fileId);
            // *4*
            Resource image = fetchImageFromUrl(imageUrl);
            // *5*
            HttpHeaders headers = createHttpHeaders();
            // *6*
            return new ResponseEntity<>(image, headers, HttpStatus.OK);
        } catch (MediaExceptionController.MediaServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error occurred while retrieving image: ", e);
            throw new MediaExceptionController.MediaServiceException("Unexpected error occurred", e);
        }
    }

    /** generateImageUrl()
     /*  Generate a URL to access the image from Google Drive using fileId. */
    private String generateImageUrl(String fileId) {
        return "https://drive.google.com/uc?export=view&id=" + fileId;
    }

    /** fetchImageFromUrl()
     /*  Fetch the image bytes from the provided URL.
     Return the image as a ByteArrayResource. */
    private Resource fetchImageFromUrl(String imageUrl) {
        try {
            byte[] imageBytes = restTemplate.getForObject(imageUrl, byte[].class);
            return new ByteArrayResource(imageBytes);
        } catch (Exception e) {
            throw new MediaExceptionController.ImageRetrievalException(imageUrl, e);
        }
    }

    /** createHttpHeaders()
     /*  Create and configure HTTP headers for the response.
     Set cache control and content type headers.
     Returns headers. */
    private HttpHeaders createHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        headers.setContentType(MediaType.IMAGE_PNG); // Assuming JPEG as default type
        return headers;
    }
}