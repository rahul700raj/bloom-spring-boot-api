package com.bloom.controller;

import com.bloom.entity.ListItem;
import com.bloom.repository.ListItemRepository;
import com.bloom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lists")
@CrossOrigin(origins = "*")
public class ListController {

    @Autowired
    private ListItemRepository listItemRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<ListItem>> getAllListItems() {
        return ResponseEntity.ok(listItemRepository.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ListItem> getListItemById(@PathVariable Long id) {
        return listItemRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<ListItem>> getListItemsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(listItemRepository.findByUserId(userId));
    }

    @GetMapping("/user/{userId}/completed")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<ListItem>> getCompletedListItemsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(listItemRepository.findByUserIdAndCompleted(userId, true));
    }

    @GetMapping("/user/{userId}/pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<ListItem>> getPendingListItemsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(listItemRepository.findByUserIdAndCompleted(userId, false));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> createListItem(@RequestBody ListItem listItem) {
        if (listItem.getUser() == null || listItem.getUser().getId() == null) {
            return ResponseEntity.badRequest().body("User ID is required");
        }

        return userRepository.findById(listItem.getUser().getId())
                .map(user -> {
                    listItem.setUser(user);
                    ListItem savedListItem = listItemRepository.save(listItem);
                    return ResponseEntity.ok(savedListItem);
                })
                .orElse(ResponseEntity.badRequest().body("User not found"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> updateListItem(@PathVariable Long id, @RequestBody ListItem listItemDetails) {
        return listItemRepository.findById(id)
                .map(listItem -> {
                    if (listItemDetails.getTitle() != null) listItem.setTitle(listItemDetails.getTitle());
                    if (listItemDetails.getDescription() != null) listItem.setDescription(listItemDetails.getDescription());
                    if (listItemDetails.getCompleted() != null) listItem.setCompleted(listItemDetails.getCompleted());
                    if (listItemDetails.getPriority() != null) listItem.setPriority(listItemDetails.getPriority());
                    if (listItemDetails.getDueDate() != null) listItem.setDueDate(listItemDetails.getDueDate());
                    
                    ListItem updatedListItem = listItemRepository.save(listItem);
                    return ResponseEntity.ok(updatedListItem);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> completeListItem(@PathVariable Long id) {
        return listItemRepository.findById(id)
                .map(listItem -> {
                    listItem.setCompleted(true);
                    listItemRepository.save(listItem);
                    return ResponseEntity.ok().body("List item marked as completed");
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/uncomplete")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> uncompleteListItem(@PathVariable Long id) {
        return listItemRepository.findById(id)
                .map(listItem -> {
                    listItem.setCompleted(false);
                    listItemRepository.save(listItem);
                    return ResponseEntity.ok().body("List item marked as pending");
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> deleteListItem(@PathVariable Long id) {
        return listItemRepository.findById(id)
                .map(listItem -> {
                    listItemRepository.delete(listItem);
                    return ResponseEntity.ok().body("List item deleted successfully");
                })
                .orElse(ResponseEntity.notFound().build());
    }
}