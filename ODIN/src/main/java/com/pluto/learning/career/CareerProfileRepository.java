package com.pluto.learning.career;

import com.pluto.learning.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository para gestión de perfiles de carrera
 */
@Repository
public interface CareerProfileRepository extends JpaRepository<CareerProfile, Long> {
    
    /**
     * Encuentra el perfil de carrera por usuario
     */
    Optional<CareerProfile> findByUser(User user);
    
    /**
     * Encuentra el perfil de carrera por ID de usuario
     */
    Optional<CareerProfile> findByUserId(Long userId);
    
    /**
     * Verifica si existe un perfil para el usuario
     */
    boolean existsByUser(User user);
}
