package com.social.anti.model;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ================================
 * MODEL (M trong MVC)
 * ================================
 * Entity Role - Đại diện cho vai trò người dùng
 * Ví dụ: ROLE_USER, ROLE_ADMIN
 */
@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Tên role - Spring Security yêu cầu prefix "ROLE_"
     * Ví dụ: ROLE_USER, ROLE_ADMIN
     */
    @Column(nullable = false, unique = true)
    private String name;

    private String displayName;
    private String description;

    public Role(String name, String displayName, String description) {
        this.name = name;
        this.displayName = displayName;
        this.description = description;
    }
}
