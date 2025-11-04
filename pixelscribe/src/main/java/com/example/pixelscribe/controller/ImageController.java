package com.example.pixelscribe.controller;

import com.example.pixelscribe.model.document.ImageEntity;
import com.example.pixelscribe.services.GeminiAIService;
import com.example.pixelscribe.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/images")
@CrossOrigin(origins = "*")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private GeminiAIService geminiAIService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(
            @RequestParam("image") MultipartFile imageFile,
            @RequestParam("userId") String userId) {

        try {
            // Validaciones básicas
            if (imageFile.isEmpty()) {
                return ResponseEntity.badRequest().body(createErrorResponse("No se proporcionó ninguna imagen"));
            }

            if (!isValidImageType(imageFile.getContentType())) {
                return ResponseEntity.badRequest().body(createErrorResponse(
                        "Tipo de archivo no válido. Use JPEG, PNG, GIF o WebP"));
            }

            if (imageFile.getSize() > 10 * 1024 * 1024) { // 10MB
                return ResponseEntity.badRequest().body(createErrorResponse(
                        "La imagen es demasiado grande. Máximo 10MB"));
            }

            // Crear registro en MongoDB
            ImageEntity imageEntity = imageService.createImage(
                    imageFile.getOriginalFilename(),
                    userId,
                    imageFile.getContentType(),
                    imageFile.getSize()
            );

            // Actualizar estado a procesando
            imageEntity.setStatus("PROCESSING");
            imageService.updateImage(imageEntity);

            // Procesar con IA
            String description;
            try {
                description = geminiAIService.analyzeImage(imageFile);
                imageEntity.setDescription(description);
                imageEntity.setStatus("COMPLETED");
            } catch (Exception e) {
                imageEntity.setStatus("ERROR");
                imageEntity.setDescription("Error en el análisis de IA: " + e.getMessage());
            }

            // Guardar resultado final
            ImageEntity savedImage = imageService.updateImage(imageEntity);

            // Crear response
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("image", createImageResponse(savedImage));
            response.put("message", "Imagen procesada exitosamente");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(createErrorResponse("Error interno del servidor: " + e.getMessage()));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserImages(@PathVariable String userId) {
        try {
            List<ImageEntity> userImages = imageService.getImagesByUserId(userId);

            List<Map<String, Object>> imagesResponse = userImages.stream()
                    .map(this::createImageResponse)
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("images", imagesResponse);
            response.put("count", imagesResponse.size());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(createErrorResponse("Error obteniendo imágenes: " + e.getMessage()));
        }
    }

    @GetMapping("/{imageId}")
    public ResponseEntity<?> getImage(@PathVariable String imageId) {
        try {
            return imageService.getImageById(imageId)
                    .map(image -> {
                        Map<String, Object> response = new HashMap<>();
                        response.put("success", true);
                        response.put("image", createImageResponse(image));
                        return ResponseEntity.ok(response);
                    })
                    .orElse(ResponseEntity.notFound().build());

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(createErrorResponse("Error obteniendo la imagen"));
        }
    }

    @DeleteMapping("/{imageId}")
    public ResponseEntity<?> deleteImage(@PathVariable String imageId) {
        try {
            if (imageService.getImageById(imageId).isPresent()) {
                imageService.deleteImage(imageId);

                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Imagen eliminada correctamente");
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(createErrorResponse("Error eliminando la imagen"));
        }
    }

    @GetMapping("/user/{userId}/stats")
    public ResponseEntity<?> getUserStats(@PathVariable String userId) {
        try {
            long totalImages = imageService.getUserImageCount(userId);
            List<ImageEntity> completedImages = imageService.getImagesByUserIdAndStatus(userId, "COMPLETED");
            List<ImageEntity> errorImages = imageService.getImagesByUserIdAndStatus(userId, "ERROR");

            Map<String, Object> stats = new HashMap<>();
            stats.put("totalImages", totalImages);
            stats.put("completed", completedImages.size());
            stats.put("errors", errorImages.size());
            stats.put("processing", totalImages - completedImages.size() - errorImages.size());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("stats", stats);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(createErrorResponse("Error obteniendo estadísticas"));
        }
    }

    // Métodos auxiliares
    private Map<String, Object> createImageResponse(ImageEntity image) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", image.getId());
        response.put("filename", image.getFilename());
        response.put("originalFilename", image.getOriginalFilename());
        response.put("description", image.getDescription());
        response.put("status", image.getStatus());
        response.put("userId", image.getUserId());
        response.put("createdAt", image.getCreatedAt().toString());
        response.put("fileSize", image.getFileSize());
        response.put("contentType", image.getContentType());
        response.put("aiModel", image.getAiModel());
        response.put("processingTime", image.getProcessingTime());
        return response;
    }

    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("error", message);
        return response;
    }

    private boolean isValidImageType(String contentType) {
        return contentType != null &&
                (contentType.equals("image/jpeg") ||
                        contentType.equals("image/png") ||
                        contentType.equals("image/gif") ||
                        contentType.equals("image/webp"));
    }
}