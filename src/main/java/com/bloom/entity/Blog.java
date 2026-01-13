package com.bloom.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "blogs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Blog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false, unique = true)
    private String slug;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    @Column(length = 500)
    private String excerpt;
    
    private String featuredImage;
    
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;
    
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    
    @ManyToOne
    @JoinColumn(name = "sub_category_id")
    private SubCategory subCategory;
    
    @Column(nullable = false)
    private String status = "DRAFT"; // DRAFT, PUBLISHED, ARCHIVED
    
    @Column(nullable = false)
    private Integer views = 0;
    
    private LocalDateTime publishedAt;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}