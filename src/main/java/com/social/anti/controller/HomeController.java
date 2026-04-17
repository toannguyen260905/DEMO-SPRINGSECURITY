package com.social.anti.controller;

import com.social.anti.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.social.anti.model.Post;

/**
 * ================================================================
 * CONTROLLER (C trong MVC) - HomeController
 * ================================================================
 * Xử lý request cho trang chủ / news feed
 * URL: / và /home
 *
 * Spring Security đã kiểm tra auth TRƯỚC khi vào đây
 * (/home yêu cầu đăng nhập - xem SecurityConfig)
 * ================================================================
 */
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final PostService postService;

    /**
     * Trang landing page (public - không cần đăng nhập)
     */
    @GetMapping("/")
    public String landingPage() {
        return "home/landing";
    }

    /**
     * News Feed (yêu cầu đăng nhập - SecurityConfig đã cấu hình)
     * Spring Security tự động redirect về /auth/login nếu chưa login
     */
    @GetMapping("/home")
    public String feed(Model model,
                       @RequestParam(defaultValue = "0") int page) {
        // Lấy bài viết public, phân trang
        Page<Post> posts = postService.getPublicPosts(page, 10);
        model.addAttribute("posts", posts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", posts.getTotalPages());
        return "home/feed";
    }
}
