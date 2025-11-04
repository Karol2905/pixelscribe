package com.example.pixelscribe.services;

import com.example.pixelscribe.model.entities.User;
import com.example.pixelscribe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Crear un nuevo usuario
     */
    public User createUser(User user) {
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    /**
     * Obtener todos los usuarios
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Buscar un usuario por su ID
     */
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    /**
     * Buscar un usuario por su email
     */
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Actualizar un usuario
     */
    public Optional<User> updateUser(String id, User updatedUser) {
        return userRepository.findById(id).map(existingUser -> {
            existingUser.setName(updatedUser.getName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setUpdatedAt(LocalDateTime.now());
            return userRepository.save(existingUser);
        });
    }

    /**
     * Eliminar un usuario por ID
     */
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}
