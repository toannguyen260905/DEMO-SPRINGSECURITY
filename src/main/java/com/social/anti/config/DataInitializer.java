package com.social.anti.config;

import com.social.anti.model.Post;
import com.social.anti.model.Role;
import com.social.anti.model.User;
import com.social.anti.repository.PostRepository;
import com.social.anti.repository.RoleRepository;
import com.social.anti.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * ================================================================
 * DataInitializer - Khởi tạo dữ liệu mẫu khi ứng dụng chạy
 * CommandLineRunner được chạy sau khi Spring Boot khởi động
 * ================================================================
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        log.info("🚀 Khởi tạo dữ liệu mẫu...");

        // ── Tạo Roles ────────────────────────────────────────
        Role userRole = new Role("ROLE_USER", "Người dùng", "Thành viên thông thường");
        Role adminRole = new Role("ROLE_ADMIN", "Quản trị viên", "Quản lý toàn hệ thống");
        roleRepository.save(userRole);
        roleRepository.save(adminRole);

        // ── Tạo Admin Account ─────────────────────────────────
        User admin = new User("Admin SocialConnect", "admin@social.com",
                passwordEncoder.encode("admin123"));
        admin.setAvatarUrl("https://api.dicebear.com/7.x/initials/svg?seed=Admin&backgroundColor=6366f1");
        admin.setBio("Quản trị viên hệ thống SocialConnect");
        admin.getRoles().add(userRole);
        admin.getRoles().add(adminRole);
        userRepository.save(admin);

        // ── Tạo User Accounts ─────────────────────────────────
        User alice = createUser("Alice Nguyễn", "user@social.com", "user123",
                "https://api.dicebear.com/7.x/initials/svg?seed=Alice&backgroundColor=ec4899",
                "Yêu thích nhiếp ảnh và du lịch 📸✈️", userRole);

        User bob = createUser("Bob Trần", "bob@social.com", "bob123",
                "https://api.dicebear.com/7.x/initials/svg?seed=Bob&backgroundColor=10b981",
                "Developer | Coffee lover ☕", userRole);

        User charlie = createUser("Charlie Lê", "charlie@social.com", "charlie123",
                "https://api.dicebear.com/7.x/initials/svg?seed=Charlie&backgroundColor=f59e0b",
                "Sinh viên CNTT | Spring Boot enthusiast", userRole);

        // ── Tạo Posts mẫu ─────────────────────────────────────
        createPost("🎉 Chào mừng đến với SocialConnect! Đây là nền tảng demo Spring Security. Hãy đăng nhập để khám phá!", admin);
        createPost("🛡️ Spring Security giúp bảo vệ ứng dụng khỏi các mối đe dọa: SQL Injection, CSRF, XSS và nhiều hơn nữa!", admin);
        createPost("☕ Hôm nay là ngày tuyệt vời để học Spring Boot! Ai đang làm việc với Spring Security? 🙋‍♂️", alice);
        createPost("📸 Vừa chụp được bộ ảnh đẹp ở Hội An. Spring Security cũng quan trọng như ánh nắng vậy - bảo vệ mọi thứ! 😄", alice);
        createPost("🚀 Tips: BCrypt password hash không thể giải mã ngược. Đây là điều làm cho Spring Security mạnh mẽ!", bob);
        createPost("💡 @PreAuthorize annotation cho phép kiểm soát quyền truy cập ngay tại method level. Cực kỳ tiện dụng!", bob);
        createPost("🎓 Đang học Spring Security cho bài thuyết trình. Kiến trúc Filter Chain thật sự ấn tượng!", charlie);

        log.info("✅ Dữ liệu mẫu đã được tạo thành công!");
        log.info("📊 Roles: {}, Users: {}, Posts: {}",
                roleRepository.count(), userRepository.count(), postRepository.count());
    }

    private User createUser(String name, String email, String password,
                             String avatar, String bio, Role role) {
        User user = new User(name, email, passwordEncoder.encode(password));
        user.setAvatarUrl(avatar);
        user.setBio(bio);
        user.getRoles().add(role);
        return userRepository.save(user);
    }

    private Post createPost(String content, User author) {
        Post post = new Post(content, author);
        post.setLikesCount((int) (Math.random() * 50));
        return postRepository.save(post);
    }
}
