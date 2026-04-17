package com.social.anti.service;

import com.social.anti.model.Post;
import com.social.anti.model.User;
import com.social.anti.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * ==============================================
 * SERVICE LAYER (Business Logic)
 * ==============================================
 * PostService - Xử lý logic bài viết
 *
 * Dùng @PreAuthorize để demo Method Security
 * - Annotation được xử lý bởi Spring Security AOP
 * ==============================================
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;

    /**
     * Tạo bài viết mới - chỉ USER đã đăng nhập mới được
     */
    @Transactional
    @PreAuthorize("isAuthenticated()")
    public Post createPost(String content, User author) {
        Post post = new Post(content, author);
        Post saved = postRepository.save(post);
        log.info("📝 Bài viết mới từ {}: {}", author.getFullName(), content.substring(0, Math.min(50, content.length())));
        return saved;
    }

    /**
     * Lấy tất cả bài viết công khai (không cần đăng nhập)
     */
    @Transactional(readOnly = true)
    public Page<Post> getPublicPosts(int page, int size) {
        return postRepository.findByVisibilityOrderByCreatedAtDesc(
                Post.PostVisibility.PUBLIC,
                PageRequest.of(page, size, Sort.by("createdAt").descending())
        );
    }

    /**
     * Lấy bài viết của một user
     */
    @Transactional(readOnly = true)
    public List<Post> getPostsByUser(User user) {
        return postRepository.findByAuthorOrderByCreatedAtDesc(user);
    }

    /**
     * Xóa bài viết - chỉ ADMIN hoặc chủ bài viết
     * @PreAuthorize demo Method-Level Security
     */
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void adminDeletePost(Long postId) {
        postRepository.deleteById(postId);
        log.info("🗑️ Admin đã xóa bài viết ID: {}", postId);
    }

    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    public long getTotalPosts() {
        return postRepository.count();
    }

    @Transactional(readOnly = true)
    public List<Post> getAllPosts() {
        return postRepository.findAll(Sort.by("createdAt").descending());
    }
}
