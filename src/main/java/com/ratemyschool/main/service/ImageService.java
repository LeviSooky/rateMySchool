package com.ratemyschool.main.service;

import com.ratemyschool.main.exception.RmsRuntimeException;
import com.ratemyschool.main.entity.ImageData;
import com.ratemyschool.main.repository.ImageRepository;
import com.ratemyschool.main.util.ImageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository repository;
    public byte[] findBy(UUID schoolId) {
        byte[] image = repository.findById(schoolId)
                .orElseThrow(() -> new RmsRuntimeException("Image not found."))
                .getImage();
        return ImageUtil.decompressImage(image);
    }

    public ImageData save(UUID schoolId, byte[] image) {
        return repository.save(new ImageData(schoolId, image));
    }
}
