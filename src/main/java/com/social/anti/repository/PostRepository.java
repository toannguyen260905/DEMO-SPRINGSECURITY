package com.social.anti.repository;

import com.social.anti.model.Post;
import com.social.anti.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ================================
 * REPOSITORY (Data Access Layer)
 * ================================
 * Spring Data JPA Repository cho Post
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByVisibilityOrderByCreatedAtDesc(Post.PostVisibility visibility, Pageable pageable);
    List<Post> findByAuthorOrderByCreatedAtDesc(User author);
    long countByAuthor(User author);
}
