package com.pluto.learning.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Buscar usuario por email
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Buscar usuario por ID de Keycloak
     */
    Optional<User> findByKeycloakId(String keycloakId);
    
    /**
     * Verificar si existe usuario con email
     */
    boolean existsByEmail(String email);
    
    /**
     * Obtener usuarios por rol
     */
    List<User> findByRole(UserRole role);
}
