package com.social.anti.controller;

import com.social.anti.service.PostService;
import com.social.anti.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * ================================================================
 * CONTROLLER (C trong MVC) - AdminController
 * ================================================================
 * Chỉ ROLE_ADMIN mới truy cập được!
 * Được bảo vệ 2 lớp:
 *   1. SecurityConfig: .requestMatchers("/admin/**").hasRole("ADMIN")
 *   2. @PreAuthorize trên từng method
 *
 * Nếu USER cố vào /admin → Spring Security trả về 403 Forbidden
 * ================================================================
 */
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")  // Bảo vệ toàn bộ controller
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final PostService postService;

    /**
     * Dashboard Admin - Tổng quan hệ thống
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalUsers", userService.getTotalUsers());
        model.addAttribute("activeUsers", userService.getActiveUsers());
        model.addAttribute("totalPosts", postService.getTotalPosts());
        model.addAttribute("recentUsers", userService.getAllUsers());
        model.addAttribute("recentPosts", postService.getAllPosts());
        return "admin/dashboard";
    }

    /**
     * Quản lý Users
     */
    @GetMapping("/users")
    public String manageUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/users";
    }

    /**
     * Khóa/mở khóa tài khoản user
     */
    @PostMapping("/users/{id}/toggle-lock")
    public String toggleLock(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userService.toggleUserLock(id);
        redirectAttributes.addFlashAttribute("success", "✅ Đã thay đổi trạng thái tài khoản!");
        return "redirect:/admin/users";
    }

    /**
     * Xóa bài viết - chỉ Admin
     */
    @PostMapping("/posts/{id}/delete")
    public String deletePost(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        postService.adminDeletePost(id);  // @PreAuthorize("hasRole('ADMIN')") trong service
        redirectAttributes.addFlashAttribute("success", "✅ Đã xóa bài viết!");
        return "redirect:/admin/dashboard";
    }
}
