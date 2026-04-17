package com.social.anti.controller;

import com.social.anti.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * ================================================================
 * CONTROLLER (C trong MVC) - AuthController
 * ================================================================
 * Xử lý Authentication (Đăng nhập/Đăng ký/Đăng xuất)
 *
 * QUAN TRỌNG: Trang /auth/login được Spring Security
 * tự động gọi khi user chưa đăng nhập
 * ================================================================
 */
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;

    /**
     * Hiển thị trang Login
     * Spring Security redirect về đây khi cần authentication
     *
     * @param error  true nếu đăng nhập sai
     * @param logout true nếu vừa đăng xuất
     */
    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error,
                            @RequestParam(required = false) String logout,
                            Model model) {
        // Nếu đã đăng nhập rồi thì không hiện trang login nữa
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()
                && !auth.getName().equals("anonymousUser")) {
            return "redirect:/home";
        }

        if (error != null) {
            model.addAttribute("errorMessage",
                "❌ Email hoặc mật khẩu không đúng. Vui lòng thử lại!");
            log.warn("🔐 Đăng nhập thất bại!");
        }
        if (logout != null) {
            model.addAttribute("successMessage", "👋 Đăng xuất thành công!");
        }
        return "auth/login";
    }

    /**
     * Hiển thị trang Đăng ký
     */
    @GetMapping("/register")
    public String registerPage(Model model) {
        return "auth/register";
    }

    /**
     * Xử lý form đăng ký (POST)
     * Sau khi lưu DB → redirect về trang login
     */
    @PostMapping("/register")
    public String processRegister(@RequestParam String fullName,
                                   @RequestParam String email,
                                   @RequestParam String password,
                                   @RequestParam String confirmPassword,
                                   RedirectAttributes redirectAttributes) {
        // Validate
        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "❌ Mật khẩu xác nhận không khớp!");
            return "redirect:/auth/register";
        }
        if (password.length() < 6) {
            redirectAttributes.addFlashAttribute("error", "❌ Mật khẩu phải có ít nhất 6 ký tự!");
            return "redirect:/auth/register";
        }

        try {
            userService.registerUser(fullName, email, password);
            redirectAttributes.addFlashAttribute("success",
                "✅ Đăng ký thành công! Hãy đăng nhập để bắt đầu.");
            return "redirect:/auth/login";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "❌ " + e.getMessage());
            return "redirect:/auth/register";
        }
    }

    /**
     * Trang profile của user đang đăng nhập
     */
    @GetMapping("/profile")
    public String profile(Authentication authentication, Model model) {
        String email = authentication.getName();
        userService.findByEmail(email).ifPresent(user -> model.addAttribute("user", user));
        return "auth/profile";
    }
}
