package com.example.pixelscribe.repository;

import com.example.pixelscribe.model.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    /**
     * Busca un usuario por su email.
     * Útil para login, validación de duplicados, etc.
     */
    Optional<User> findByEmail(String email);

    /**
     * Verifica si un usuario con ese email ya existe.
     */
    boolean existsByEmail(String email);
}
