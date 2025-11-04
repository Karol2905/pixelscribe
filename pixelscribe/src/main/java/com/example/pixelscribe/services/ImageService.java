package com.example.pixelscribe.services;

import com.example.pixelscribe.model.entities.ImageEntity;
import com.example.pixelscribe.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    public ImageEntity createImage(String originalFilename, String userId, String contentType, Long fileSize) {
        ImageEntity image = new ImageEntity(originalFilename, userId, contentType, fileSize);
        return imageRepository.save(image);
    }

    public ImageEntity updateImage(ImageEntity image) {
        return imageRepository.save(image);
    }

    public List<ImageEntity> getImagesByUserId(String userId) {
        return imageRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public Optional<ImageEntity> getImageById(String id) {
        return imageRepository.findById(id);
    }

    public List<ImageEntity> getImagesByUserIdAndStatus(String userId, String status) {
        return imageRepository.findByUserIdAndStatus(userId, status);
    }

    public List<ImageEntity> getImagesByStatus(String status) {
        return imageRepository.findByStatus(status);
    }

    public long getUserImageCount(String userId) {
        return imageRepository.countByUserId(userId);
    }

    public void deleteImage(String id) {
        imageRepository.deleteById(id);
    }

    public List<ImageEntity> searchImagesByFilename(String userId, String searchTerm) {
        return imageRepository.findByOriginalFilenameContainingAndUserId(searchTerm, userId);
    }

    public ImageEntity updateImageStatus(String id, String status) {
        Optional<ImageEntity> imageOpt = imageRepository.findById(id);
        if (imageOpt.isPresent()) {
            ImageEntity image = imageOpt.get();
            image.setStatus(status);
            return imageRepository.save(image);
        }
        return null;
    }

    public ImageEntity completeImageProcessing(String id, String description, Long processingTime) {
        Optional<ImageEntity> imageOpt = imageRepository.findById(id);
        if (imageOpt.isPresent()) {
            ImageEntity image = imageOpt.get();
            image.setDescription(description);
            image.setStatus("COMPLETED");
            image.setProcessingTime(processingTime);
            return imageRepository.save(image);
        }
        return null;
    }
}