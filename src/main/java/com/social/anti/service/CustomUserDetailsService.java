package com.social.anti.service;

import com.social.anti.model.Role;
import com.social.anti.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.social.anti.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * ==============================================
 * SERVICE LAYER (Business Logic)
 * ==============================================
 * CustomUserDetailsService - Bắt buộc phải implement
 * khi dùng Spring Security với database tùy chỉnh.
 *
 * Spring Security sẽ gọi loadUserByUsername() mỗi khi
 * người dùng đăng nhập để lấy thông tin xác thực.
 *
 * LUỒNG AUTHENTICATION:
 * 1. User nhập email + password
 * 2. Spring Security gọi loadUserByUsername(email)
 * 3. Method này trả về UserDetails từ DB
 * 4. Spring Security so sánh password (BCrypt)
 * 5. Nếu đúng → tạo SecurityContext với Authentication object
 * ==============================================
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Load user từ DB theo email (username)
     * Spring Security dùng method này để xác thực
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("🔐 Spring Security đang tìm user theo email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("❌ Không tìm thấy user với email: {}", email);
                    return new UsernameNotFoundException("Không tìm thấy tài khoản: " + email);
                });

        log.debug("✅ Tìm thấy user: {} với roles: {}", user.getFullName(), user.getRoles());

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())  // đã được BCrypt hash
                .authorities(getAuthorities(user.getRoles()))
                .accountExpired(false)
                .accountLocked(!user.isAccountNonLocked())
                .credentialsExpired(false)
                .disabled(!user.isEnabled())
                .build();
    }

    /**
     * Chuyển đổi Set<Role> thành Collection<GrantedAuthority>
     * Spring Security dùng GrantedAuthority để kiểm tra phân quyền
     */
    private Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}
