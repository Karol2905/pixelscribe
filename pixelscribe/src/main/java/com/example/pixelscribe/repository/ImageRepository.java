package com.example.pixelscribe.repository;

import com.example.pixelscribe.model.entities.ImageEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends MongoRepository<ImageEntity, String> {

    // Encontrar imágenes por usuario, ordenadas por fecha de creación descendente
    List<ImageEntity> findByUserIdOrderByCreatedAtDesc(String userId);

    // Encontrar imágenes por estado
    List<ImageEntity> findByStatus(String status);

    // Encontrar imágenes por usuario y estado
    List<ImageEntity> findByUserIdAndStatus(String userId, String status);

    // Consulta personalizada para búsqueda por filename
    @Query("{ 'original_filename': { $regex: ?0, $options: 'i' }, 'user_id': ?1 }")
    List<ImageEntity> findByOriginalFilenameContainingAndUserId(String filename, String userId);

    // Contar imágenes por usuario
    long countByUserId(String userId);

    // Eliminar imágenes por usuario
    void deleteByUserId(String userId);
}