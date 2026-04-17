package com.social.anti.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * ================================
 * MODEL (M trong MVC)
 * ================================
 * Entity Post - Bài viết trên mạng xã hội
 */
@Entity
@Table(name = "posts")
@Data
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 1000)
    private String content;

    private String imageUrl;

    /**
     * Loại bài viết để phân quyền demo
     * PUBLIC: ai cũng xem được
     * PRIVATE: chỉ chủ bài xem
     */
    @Enumerated(EnumType.STRING)
    private PostVisibility visibility = PostVisibility.PUBLIC;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private int likesCount = 0;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Post(String content, User author) {
        this.content = content;
        this.author = author;
    }

    public enum PostVisibility {
        PUBLIC, PRIVATE
    }
}
