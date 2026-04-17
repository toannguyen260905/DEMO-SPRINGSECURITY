package com.social.anti.config;

import com.social.anti.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * ================================================================
 *  🛡️  SPRING SECURITY CONFIGURATION - Trọng tâm của bài demo
 * ================================================================
 *
 * @Configuration  → Đây là class cấu hình Spring
 * @EnableWebSecurity → Kích hoạt Spring Security cho web app
 * @EnableMethodSecurity → Cho phép @PreAuthorize trên method
 *
 * Spring Security hoạt động dựa trên FILTER CHAIN:
 * Request → Filter1 → Filter2 → ... → Controller
 *
 *  Các Filter quan trọng:
 *  - UsernamePasswordAuthenticationFilter: xử lý form login
 *  - BasicAuthenticationFilter: xử lý Basic Auth
 *  - AnonymousAuthenticationFilter: gán Anonymous nếu chưa login
 *  - ExceptionTranslationFilter: xử lý 403/401
 *  - FilterSecurityInterceptor: kiểm tra quyền
 * ================================================================
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)  // Spring Security 5.x annotation
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    /**
     * ✅ Bean 1: PasswordEncoder
     * BCrypt là thuật toán hash an toàn cho password
     * - Tự động thêm salt (chống rainbow table attack)
     * - Work factor = 10 (càng cao càng chậm brute-force)
     * - Mỗi lần hash ra kết quả khác nhau
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    /**
     * ✅ Bean 2: DaoAuthenticationProvider
     * Kết nối UserDetailsService với PasswordEncoder
     * Spring Security dùng Provider này để xác thực
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);  // Biết cách lấy user từ DB
        provider.setPasswordEncoder(passwordEncoder());       // Biết cách so sánh password
        return provider;
    }

    /**
     * ✅ Bean 3: AuthenticationManager
     * Quản lý quá trình authentication
     * Controller có thể inject để login thủ công
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * ✅ Bean 4: SecurityFilterChain - QUAN TRỌNG NHẤT
     * Định nghĩa toàn bộ luật bảo mật của ứng dụng
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // ── [1] AUTHORIZATION RULES ─────────────────────────────────
            .authorizeRequests(auth -> auth
                .antMatchers(
                    "/",
                    "/auth/login",
                    "/auth/register",
                    "/auth/login-error",
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/h2-console/**"
                ).permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )

            // ── [2] FORM LOGIN ───────────────────────────────────────────
            .formLogin(form -> form
                .loginPage("/auth/login")
                .loginProcessingUrl("/auth/do-login")
                .usernameParameter("email")
                .passwordParameter("password")
                .successHandler(customSuccessHandler())
                .failureUrl("/auth/login?error=true")
                .permitAll()
            )

            // ── [3] LOGOUT ───────────────────────────────────────────────
            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/auth/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "remember-me")
                .permitAll()
            )

            // ── [4] REMEMBER ME ─────────────────────────────────────────
            .rememberMe(remember -> remember
                .userDetailsService(userDetailsService)
                .key("socialconnect-secret-key-2024")
                .tokenValiditySeconds(7 * 24 * 60 * 60)
                .rememberMeParameter("remember-me")
            )

            // ── [5] CSRF - disable for H2 console ────────────────────────
            .csrf(csrf -> csrf
                .ignoringAntMatchers("/h2-console/**")
            )

            // ── [6] SESSION MANAGEMENT ───────────────────────────────────
            .sessionManagement(session -> session
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
            )

            // ── [7] H2 Console (iframe fix) ──────────────────────────────
            .headers(headers -> headers
                .frameOptions().sameOrigin()
            )

            // ── [8] AUTH PROVIDER ────────────────────────────────────────
            .authenticationProvider(authenticationProvider());

        return http.build();
    }

    /**
     * Custom Success Handler - Xử lý sau khi login thành công
     * Admin → /admin/dashboard
     * User  → /home
     */
    @Bean
    public AuthenticationSuccessHandler customSuccessHandler() {
        return (request, response, authentication) -> {
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            if (isAdmin) {
                response.sendRedirect("/admin/dashboard");
            } else {
                response.sendRedirect("/home");
            }
        };
    }
}
