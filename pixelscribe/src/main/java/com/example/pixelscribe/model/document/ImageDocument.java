package com.example.pixelscribe.model.document;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "images")
public class ImageDocument {
    @Id
    private String id;

    @Field("filename")
    private String filename;

    @Field("description")
    private String description;

    @Field("status")
    private String status; // "PROCESSING", "COMPLETED", "ERROR"

    @Field("user_id")
    private String userId;

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("file_size")
    private Long fileSize;

    @Field("content_type")
    private String contentType;

    public ImageDocument() {
        this.createdAt = LocalDateTime.now();
        this.status = "PROCESSING";
    }

    public ImageDocument(String filename, String userId, String contentType, Long fileSize) {
        this();
        this.filename = filename;
        this.userId = userId;
        this.contentType = contentType;
        this.fileSize = fileSize;
    }
}