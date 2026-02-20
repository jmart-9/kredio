package com.kredio.service;

import com.kredio.model.User;
import com.kredio.model.Role;
import com.kredio.model.Tenant;
import com.kredio.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registra un nuevo usuario (RF: Gestión de Usuarios por Rol)
     * - Valida email único
     * - Hashea la contraseña
     * - Asigna tenant y rol
     */
    @Transactional
    public User registerUser(String name, String email, String rawPassword, Role role, Tenant tenant) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists: " + email);
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(role);
        user.setTenant(tenant);
        user.setActive(true);

        return userRepository.save(user);
    }

    /**
     * Busca usuario por email (usado en login y JWT)
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Implementación de UserDetailsService para Spring Security
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities("ROLE_" + user.getRole().name())
                .accountExpired(!user.isActive())
                .accountLocked(!user.isActive())
                .credentialsExpired(false)
                .disabled(!user.isActive())
                .build();
    }

    /**
     * Cambia la contraseña de un usuario (para recuperación o cambio manual)
     */
    @Transactional
    public void changePassword(Long userId, String newRawPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        user.setPassword(passwordEncoder.encode(newRawPassword));
        userRepository.save(user);
    }

    /**
     * Activa o desactiva un usuario (admin action)
     */
    @Transactional
    public void setUserActive(Long userId, boolean active) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        user.setActive(active);
        userRepository.save(user);
    }

    // Métodos futuros que puedes agregar según avances:
    // - findByTenantId(Long tenantId)
    // - updateRole(Long userId, Role newRole)
    // - recoverPassword(String email) → envía token temporal via email/SMS
}