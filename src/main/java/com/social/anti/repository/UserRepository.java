package com.social.anti.repository;

import com.social.anti.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * ================================
 * REPOSITORY (Data Access Layer)
 * ================================
 * Spring Data JPA Repository cho User
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    long countByEnabledTrue();
}
