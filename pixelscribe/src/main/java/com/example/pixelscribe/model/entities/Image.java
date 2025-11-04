package com.example.pixelscribe.model.entities;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "images")
public class Image {

    @Id
    private String id;

    private String filename;
    private String url;
    private String description;

    private imageStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @DBRef
    private User user;



}
