package com.example.pixelscribe.dto.response;



public class UserResponseDTO {

    private String userId;
    private String email;

    public UserResponseDTO() {}

    public UserResponseDTO(String userId, String email) {
        this.userId = userId;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
