package com.bloom.repository;

import com.bloom.entity.ListItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ListItemRepository extends JpaRepository<ListItem, Long> {
    List<ListItem> findByUserId(Long userId);
    List<ListItem> findByUserIdAndCompleted(Long userId, Boolean completed);
}