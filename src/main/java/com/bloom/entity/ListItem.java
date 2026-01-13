package com.bloom.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "list_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(length = 1000)
    private String description;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false)
    private Boolean completed = false;
    
    @Column(nullable = false)
    private Integer priority = 0; // 0: Low, 1: Medium, 2: High
    
    private LocalDateTime dueDate;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}