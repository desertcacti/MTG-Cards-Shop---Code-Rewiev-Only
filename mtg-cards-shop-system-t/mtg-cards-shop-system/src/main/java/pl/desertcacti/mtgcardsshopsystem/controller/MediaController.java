package pl.desertcacti.mtgcardsshopsystem.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import pl.desertcacti.mtgcardsshopsystem.service.media.MediaService;

/** MediaController class handles media-related requests such as retrieving images by fileName. */
@RestController
@RequestMapping("/api/media")
@Slf4j
public class MediaController {
    private final MediaService mediaService;

    @Autowired
    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    /** Retrieve fileImage by fileName from external services. */
    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> getImageByName(@PathVariable String fileName) {
        return mediaService.getImageByName(fileName);
    }
}