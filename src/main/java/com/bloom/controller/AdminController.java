package com.bloom.controller;

import com.bloom.entity.Blog;
import com.bloom.entity.Category;
import com.bloom.entity.User;
import com.bloom.repository.BlogRepository;
import com.bloom.repository.CategoryRepository;
import com.bloom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboard() {
        Map<String, Object> dashboard = new HashMap<>();
        
        dashboard.put("totalUsers", userRepository.count());
        dashboard.put("totalBlogs", blogRepository.count());
        dashboard.put("totalCategories", categoryRepository.count());
        dashboard.put("publishedBlogs", blogRepository.findByStatus("PUBLISHED").size());
        dashboard.put("draftBlogs", blogRepository.findByStatus("DRAFT").size());
        
        return ResponseEntity.ok(dashboard);
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/blogs")
    public ResponseEntity<?> getAllBlogs() {
        return ResponseEntity.ok(blogRepository.findAll());
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getAllCategories() {
        return ResponseEntity.ok(categoryRepository.findAll());
    }

    @PatchMapping("/users/{id}/role")
    public ResponseEntity<?> updateUserRole(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String role = request.get("role");
        return userRepository.findById(id)
                .map(user -> {
                    user.setRole(role);
                    userRepository.save(user);
                    return ResponseEntity.ok().body("User role updated to " + role);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/blogs/{id}")
    public ResponseEntity<?> deleteBlog(@PathVariable Long id) {
        return blogRepository.findById(id)
                .map(blog -> {
                    blogRepository.delete(blog);
                    return ResponseEntity.ok().body("Blog deleted successfully");
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        return categoryRepository.findById(id)
                .map(category -> {
                    categoryRepository.delete(category);
                    return ResponseEntity.ok().body("Category deleted successfully");
                })
                .orElse(ResponseEntity.notFound().build());
    }
}