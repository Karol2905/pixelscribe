package com.example.pixelscribe.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ImageAnalysisResponse {
    private Long id;
    private String filename;
    private String description;
    private String status;
    private String createdAt;

}