package com.enigma.bank_sampah.controller;

import com.enigma.bank_sampah.constant.APIUrl;
import com.enigma.bank_sampah.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Image", description = "API for Image")
public class ImageController {
    private final ImageService imageService;

    @Operation(
            summary = "Download image",
            description = "Download image"
    )
    @GetMapping(path = APIUrl.STUFF_IMAGE_API + "/{imageId}")
    public ResponseEntity<?> downloadStuff(
            @PathVariable String imageId
    ) {
        Resource resource = imageService.getById(imageId);

        String headerValue = String.format("attachment; filename=%s", resource.getFilename());

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION,headerValue)
                .body(resource);
    }

    @Operation(
            summary = "Download image",
            description = "Download image"
    )
    @GetMapping(path = APIUrl.TRANSACTION_IMAGE__API + "/{imageId}")
    public ResponseEntity<?> downloadTransaction(
            @PathVariable String imageId
    ) {
        Resource resource = imageService.getById(imageId);

        String headerValue = String.format("attachment; filename=%s", resource.getFilename());

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION,headerValue)
                .body(resource);
    }
}
