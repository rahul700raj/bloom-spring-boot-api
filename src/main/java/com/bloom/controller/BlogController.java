package com.bloom.controller;

import com.bloom.entity.Blog;
import com.bloom.repository.BlogRepository;
import com.bloom.repository.CategoryRepository;
import com.bloom.repository.SubCategoryRepository;
import com.bloom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/blogs")
@CrossOrigin(origins = "*")
public class BlogController {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @GetMapping
    public ResponseEntity<List<Blog>> getAllBlogs() {
        return ResponseEntity.ok(blogRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Blog> getBlogById(@PathVariable Long id) {
        return blogRepository.findById(id)
                .map(blog -> {
                    blog.setViews(blog.getViews() + 1);
                    blogRepository.save(blog);
                    return ResponseEntity.ok(blog);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<Blog> getBlogBySlug(@PathVariable String slug) {
        return blogRepository.findBySlug(slug)
                .map(blog -> {
                    blog.setViews(blog.getViews() + 1);
                    blogRepository.save(blog);
                    return ResponseEntity.ok(blog);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<Blog>> getBlogsByAuthor(@PathVariable Long authorId) {
        return ResponseEntity.ok(blogRepository.findByAuthorId(authorId));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Blog>> getBlogsByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(blogRepository.findByCategoryId(categoryId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Blog>> getBlogsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(blogRepository.findByStatus(status.toUpperCase()));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> createBlog(@RequestBody Blog blog) {
        if (blog.getAuthor() == null || blog.getAuthor().getId() == null) {
            return ResponseEntity.badRequest().body("Author ID is required");
        }

        return userRepository.findById(blog.getAuthor().getId())
                .map(author -> {
                    blog.setAuthor(author);
                    
                    if (blog.getSlug() == null || blog.getSlug().isEmpty()) {
                        blog.setSlug(blog.getTitle().toLowerCase().replaceAll("\\s+", "-"));
                    }
                    
                    if (blog.getCategory() != null && blog.getCategory().getId() != null) {
                        categoryRepository.findById(blog.getCategory().getId())
                                .ifPresent(blog::setCategory);
                    }
                    
                    if (blog.getSubCategory() != null && blog.getSubCategory().getId() != null) {
                        subCategoryRepository.findById(blog.getSubCategory().getId())
                                .ifPresent(blog::setSubCategory);
                    }
                    
                    if ("PUBLISHED".equals(blog.getStatus()) && blog.getPublishedAt() == null) {
                        blog.setPublishedAt(LocalDateTime.now());
                    }
                    
                    Blog savedBlog = blogRepository.save(blog);
                    return ResponseEntity.ok(savedBlog);
                })
                .orElse(ResponseEntity.badRequest().body("Author not found"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> updateBlog(@PathVariable Long id, @RequestBody Blog blogDetails) {
        return blogRepository.findById(id)
                .map(blog -> {
                    if (blogDetails.getTitle() != null) blog.setTitle(blogDetails.getTitle());
                    if (blogDetails.getSlug() != null) blog.setSlug(blogDetails.getSlug());
                    if (blogDetails.getContent() != null) blog.setContent(blogDetails.getContent());
                    if (blogDetails.getExcerpt() != null) blog.setExcerpt(blogDetails.getExcerpt());
                    if (blogDetails.getFeaturedImage() != null) blog.setFeaturedImage(blogDetails.getFeaturedImage());
                    if (blogDetails.getStatus() != null) {
                        blog.setStatus(blogDetails.getStatus());
                        if ("PUBLISHED".equals(blogDetails.getStatus()) && blog.getPublishedAt() == null) {
                            blog.setPublishedAt(LocalDateTime.now());
                        }
                    }
                    
                    if (blogDetails.getCategory() != null && blogDetails.getCategory().getId() != null) {
                        categoryRepository.findById(blogDetails.getCategory().getId())
                                .ifPresent(blog::setCategory);
                    }
                    
                    if (blogDetails.getSubCategory() != null && blogDetails.getSubCategory().getId() != null) {
                        subCategoryRepository.findById(blogDetails.getSubCategory().getId())
                                .ifPresent(blog::setSubCategory);
                    }
                    
                    Blog updatedBlog = blogRepository.save(blog);
                    return ResponseEntity.ok(updatedBlog);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteBlog(@PathVariable Long id) {
        return blogRepository.findById(id)
                .map(blog -> {
                    blogRepository.delete(blog);
                    return ResponseEntity.ok().body("Blog deleted successfully");
                })
                .orElse(ResponseEntity.notFound().build());
    }
}