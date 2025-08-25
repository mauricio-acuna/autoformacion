package com.pluto.learning.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService {
    
    private final UserRepository userRepository;
    
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    /**
     * Obtener usuario actual desde el contexto de autenticación
     */
    public User getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("Usuario no autenticado");
        }
        
        // Extraer información del JWT token
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String keycloakId = jwt.getClaimAsString("sub");
        String email = jwt.getClaimAsString("email");
        String name = jwt.getClaimAsString("name");
        
        // Buscar usuario existente o crear uno nuevo
        Optional<User> existingUser = userRepository.findByKeycloakId(keycloakId);
        
        if (existingUser.isPresent()) {
            return existingUser.get();
        } else {
            // Crear nuevo usuario si no existe
            User newUser = new User();
            newUser.setKeycloakId(keycloakId);
            newUser.setEmail(email);
            newUser.setName(name);
            newUser.setRole(UserRole.LEARNER); // Rol por defecto
            
            return userRepository.save(newUser);
        }
    }
    
    /**
     * Obtener usuario por ID
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    
    /**
     * Obtener usuario por email
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    /**
     * Crear nuevo usuario
     */
    public User createUser(String email, String name, UserRole role) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Ya existe un usuario con este email");
        }
        
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setRole(role);
        
        return userRepository.save(user);
    }
    
    /**
     * Actualizar rol de usuario
     */
    public User updateUserRole(Long userId, UserRole role) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        
        user.setRole(role);
        return userRepository.save(user);
    }
}
