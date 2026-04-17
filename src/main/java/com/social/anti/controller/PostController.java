package com.social.anti.controller;

import com.social.anti.model.User;
import com.social.anti.service.PostService;
import com.social.anti.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * ================================================================
 * CONTROLLER (C trong MVC) - PostController
 * ================================================================
 * Xử lý tạo và xem bài viết
 *
 * URL /post/** yêu cầu đã đăng nhập (xem SecurityConfig)
 * ================================================================
 */
@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserService userService;

    /**
     * Form tạo bài viết mới
     */
    @GetMapping("/create")
    public String createPostForm() {
        return "post/create";
    }

    /**
     * Xử lý submit bài viết
     * Authentication object được inject tự động bởi Spring Security
     */
    @PostMapping("/create")
    public String createPost(@RequestParam String content,
                              Authentication authentication,
                              RedirectAttributes redirectAttributes) {
        if (content == null || content.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Nội dung bài viết không được trống!");
            return "redirect:/post/create";
        }

        // Lấy user đang đăng nhập từ SecurityContext
        String email = authentication.getName();
        User currentUser = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        // PostService.createPost() có @PreAuthorize("isAuthenticated()")
        postService.createPost(content.trim(), currentUser);

        redirectAttributes.addFlashAttribute("success", "✅ Đăng bài thành công!");
        return "redirect:/home";
    }
}
