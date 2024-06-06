package pl.desertcacti.mtgcardsshopsystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
@ControllerAdvice
public class MediaExceptionController {

    @ExceptionHandler(MediaNotFoundException.class)
    public ResponseEntity<String> handleMediaNotFoundException(MediaNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidFileIdException.class)
    public ResponseEntity<String> handleInvalidFileIdException(InvalidFileIdException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ImageRetrievalException.class)
    public ResponseEntity<String> handleImageRetrievalException(ImageRetrievalException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MediaServiceException.class)
    public ResponseEntity<String> handleMediaServiceException(MediaServiceException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static class MediaServiceException extends RuntimeException {
        public MediaServiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    public static class MediaNotFoundException extends MediaServiceException {
        public MediaNotFoundException(String fileName) {
            super("Media not found: " + fileName, null);
        }
    }
    public static class InvalidFileIdException extends MediaServiceException {
        public InvalidFileIdException(String fileName) {
            super("File ID is not available for media: " + fileName, null);
        }
    }
    public static class ImageRetrievalException extends MediaServiceException {
        public ImageRetrievalException(String imageUrl, Throwable cause) {
            super("Error while retrieving image from URL: " + imageUrl, cause);
        }
    }
}