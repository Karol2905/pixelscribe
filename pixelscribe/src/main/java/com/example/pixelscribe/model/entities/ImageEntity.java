package com.example.pixelscribe.model.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "images")
public class ImageEntity {
    @Id
    private String id;

    @Field("filename")
    private String filename;

    @Field("original_filename")
    private String originalFilename;

    @Field("description")
    private String description;

    @Field("status")
    private String status; // "UPLOADED", "PROCESSING", "COMPLETED", "ERROR"

    @Field("user_id")
    private String userId;

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("file_size")
    private Long fileSize;

    @Field("content_type")
    private String contentType;

    @Field("ai_model")
    private String aiModel;

    @Field("processing_time")
    private Long processingTime; // en milisegundos

    // Constructores
    public ImageEntity() {
        this.createdAt = LocalDateTime.now();
        this.status = "UPLOADED";
        this.aiModel = "gemini-pro-vision";
    }

    public ImageEntity(String originalFilename, String userId, String contentType, Long fileSize) {
        this();
        this.originalFilename = originalFilename;
        this.filename = generateFilename(originalFilename);
        this.userId = userId;
        this.contentType = contentType;
        this.fileSize = fileSize;
    }

    private String generateFilename(String originalFilename) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        return "image_" + timestamp + extension;
    }
}