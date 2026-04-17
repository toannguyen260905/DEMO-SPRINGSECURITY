package com.social.anti.service;

import com.social.anti.model.Role;
import com.social.anti.model.User;
import com.social.anti.repository.RoleRepository;
import com.social.anti.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * ==============================================
 * SERVICE LAYER (Business Logic)
 * ==============================================
 * UserService - Xử lý logic liên quan đến người dùng
 * Tương tác với UserRepository và RoleRepository
 * ==============================================
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;  // Được inject từ SecurityConfig

    /**
     * Đăng ký tài khoản mới
     * - Kiểm tra email đã tồn tại chưa
     * - Mã hóa password bằng BCrypt trước khi lưu
     * - Gán role ROLE_USER mặc định
     */
    @Transactional
    public User registerUser(String fullName, String email, String rawPassword) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email đã được đăng ký: " + email);
        }

        User user = new User(fullName, email, passwordEncoder.encode(rawPassword));

        // BCrypt encoding demo
        log.info("🔒 Password gốc: {} ký tự", rawPassword.length());
        log.info("🔒 BCrypt hash: {}", passwordEncoder.encode(rawPassword));

        // Gán role mặc định
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Không tìm thấy role ROLE_USER"));
        user.getRoles().add(userRole);

        User savedUser = userRepository.save(user);
        log.info("✅ Đăng ký thành công: {} ({})", fullName, email);
        return savedUser;
    }

    /**
     * Cập nhật thời gian đăng nhập cuối
     */
    @Transactional
    public void updateLastLogin(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            user.setLastLoginAt(LocalDateTime.now());
            userRepository.save(user);
        });
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public long getTotalUsers() {
        return userRepository.count();
    }

    public long getActiveUsers() {
        return userRepository.countByEnabledTrue();
    }

    /**
     * Khóa/mở khóa tài khoản - chỉ ADMIN mới được làm
     */
    @Transactional
    public void toggleUserLock(Long userId) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setAccountNonLocked(!user.isAccountNonLocked());
            userRepository.save(user);
            log.info("Admin {} tài khoản user ID: {}",
                    user.isAccountNonLocked() ? "mở khóa" : "khóa", userId);
        });
    }
}
