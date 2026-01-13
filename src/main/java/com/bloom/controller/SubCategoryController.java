package com.bloom.controller;

import com.bloom.entity.SubCategory;
import com.bloom.repository.CategoryRepository;
import com.bloom.repository.SubCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subcategories")
@CrossOrigin(origins = "*")
public class SubCategoryController {

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity<List<SubCategory>> getAllSubCategories() {
        return ResponseEntity.ok(subCategoryRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubCategory> getSubCategoryById(@PathVariable Long id) {
        return subCategoryRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<SubCategory>> getSubCategoriesByCategoryId(@PathVariable Long categoryId) {
        return ResponseEntity.ok(subCategoryRepository.findByCategoryId(categoryId));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<SubCategory> getSubCategoryBySlug(@PathVariable String slug) {
        return subCategoryRepository.findBySlug(slug)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createSubCategory(@RequestBody SubCategory subCategory) {
        if (subCategory.getCategory() == null || subCategory.getCategory().getId() == null) {
            return ResponseEntity.badRequest().body("Category ID is required");
        }

        return categoryRepository.findById(subCategory.getCategory().getId())
                .map(category -> {
                    subCategory.setCategory(category);
                    
                    if (subCategory.getSlug() == null || subCategory.getSlug().isEmpty()) {
                        subCategory.setSlug(subCategory.getName().toLowerCase().replaceAll("\\s+", "-"));
                    }
                    
                    SubCategory savedSubCategory = subCategoryRepository.save(subCategory);
                    return ResponseEntity.ok(savedSubCategory);
                })
                .orElse(ResponseEntity.badRequest().body("Category not found"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateSubCategory(@PathVariable Long id, @RequestBody SubCategory subCategoryDetails) {
        return subCategoryRepository.findById(id)
                .map(subCategory -> {
                    if (subCategoryDetails.getName() != null) subCategory.setName(subCategoryDetails.getName());
                    if (subCategoryDetails.getDescription() != null) subCategory.setDescription(subCategoryDetails.getDescription());
                    if (subCategoryDetails.getSlug() != null) subCategory.setSlug(subCategoryDetails.getSlug());
                    if (subCategoryDetails.getActive() != null) subCategory.setActive(subCategoryDetails.getActive());
                    
                    if (subCategoryDetails.getCategory() != null && subCategoryDetails.getCategory().getId() != null) {
                        categoryRepository.findById(subCategoryDetails.getCategory().getId())
                                .ifPresent(subCategory::setCategory);
                    }
                    
                    SubCategory updatedSubCategory = subCategoryRepository.save(subCategory);
                    return ResponseEntity.ok(updatedSubCategory);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteSubCategory(@PathVariable Long id) {
        return subCategoryRepository.findById(id)
                .map(subCategory -> {
                    subCategoryRepository.delete(subCategory);
                    return ResponseEntity.ok().body("SubCategory deleted successfully");
                })
                .orElse(ResponseEntity.notFound().build());
    }
}