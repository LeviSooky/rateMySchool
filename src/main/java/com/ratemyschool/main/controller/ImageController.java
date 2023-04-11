package com.ratemyschool.main.controller;

import com.ratemyschool.main.service.ImageService;
import com.ratemyschool.main.service.SchoolService;
import com.ratemyschool.main.util.ImageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("api/image")
@RequiredArgsConstructor
@Log4j2
public class ImageController {

    private final ImageService imageService;
    private final SchoolService schoolService;

    @PostMapping
    public ResponseEntity<Void> uploadImage(@RequestParam UUID schoolId, @RequestParam MultipartFile image) {
        schoolService.findBy(schoolId);
        byte[] bytes;
        try {
            bytes = ImageUtil.compressImage(image.getBytes());
        } catch (IOException e) {
            return ResponseEntity.unprocessableEntity().build();
        }
        imageService.save(schoolId, bytes);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/{schoolId}")
    public ResponseEntity<byte[]> findImageBy(@PathVariable UUID schoolId) {
        log.info("REST request to find image by school id: {}", schoolId);
        byte[] image = imageService.findBy(schoolId);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(image);
    }
}
