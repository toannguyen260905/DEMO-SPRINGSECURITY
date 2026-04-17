package com.social.anti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ============================================
 * SocialConnect - Spring Security Demo App
 * ============================================
 * Kiến trúc MVC:
 *   Model      → com.social.anti.model
 *   View       → src/main/resources/templates (Thymeleaf)
 *   Controller → com.social.anti.controller
 *
 * Spring Security:
 *   - Authentication (Login/Register)
 *   - Authorization (ROLE_USER, ROLE_ADMIN)
 *   - BCrypt Password Encoding
 *   - Remember Me
 *   - CSRF Protection
 *   - Custom UserDetailsService
 * ============================================
 */
@SpringBootApplication
public class AntiApplication {
    public static void main(String[] args) {
        SpringApplication.run(AntiApplication.class, args);
        System.out.println("\n========================================");
        System.out.println("  🛡️  SocialConnect đã khởi động!");
        System.out.println("  URL: http://localhost:8080");
        System.out.println("  H2 Console: http://localhost:8080/h2-console");
        System.out.println("  Admin: admin@social.com / admin123");
        System.out.println("  User:  user@social.com  / user123");
        System.out.println("========================================\n");
    }
}
