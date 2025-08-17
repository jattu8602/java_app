package com.example.myapp.repository;

import com.example.myapp.model.Todo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends MongoRepository<Todo, String> {

    // Find todos by completion status
    List<Todo> findByCompleted(boolean completed);

    // Find todos by priority
    List<Todo> findByPriority(int priority);

    // Find todos by title containing text (case-insensitive)
    List<Todo> findByTitleContainingIgnoreCase(String title);

    // Find todos by description containing text (case-insensitive)
    List<Todo> findByDescriptionContainingIgnoreCase(String description);

    // Find todos by priority and completion status
    List<Todo> findByPriorityAndCompleted(int priority, boolean completed);

    // Custom query to find high priority incomplete todos
    @Query("{'priority': 3, 'completed': false}")
    List<Todo> findHighPriorityIncompleteTodos();

    // Custom query to find todos created in the last 7 days
    @Query("{'createdAt': {$gte: ?0}}")
    List<Todo> findTodosCreatedAfter(java.time.LocalDateTime date);

    // Count todos by completion status
    long countByCompleted(boolean completed);

    // Count todos by priority
    long countByPriority(int priority);
}
