package com.social.anti.repository;

import com.social.anti.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * ================================
 * REPOSITORY (Data Access Layer)
 * ================================
 * Spring Data JPA tự động tạo implementation
 * Không cần viết SQL thủ công
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
