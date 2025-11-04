package com.example.pixelscribe.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ImageUploadRequest {
    private MultipartFile image;
}