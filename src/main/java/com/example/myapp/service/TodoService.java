package com.example.myapp.service;

import com.example.myapp.dto.TodoRequest;
import com.example.myapp.model.Todo;
import com.example.myapp.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    // Create a new todo
    public Todo createTodo(TodoRequest todoRequest) {
        Todo todo = new Todo();
        todo.setTitle(todoRequest.getTitle());
        todo.setDescription(todoRequest.getDescription());
        todo.setPriority(todoRequest.getPriority());
        todo.setCreatedAt(LocalDateTime.now());
        todo.setUpdatedAt(LocalDateTime.now());

        return todoRepository.save(todo);
    }

    // Get all todos
    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    // Get todo by ID
    public Optional<Todo> getTodoById(String id) {
        return todoRepository.findById(id);
    }

    // Update todo
    public Optional<Todo> updateTodo(String id, TodoRequest todoRequest) {
        Optional<Todo> existingTodo = todoRepository.findById(id);

        if (existingTodo.isPresent()) {
            Todo todo = existingTodo.get();
            todo.setTitle(todoRequest.getTitle());
            todo.setDescription(todoRequest.getDescription());
            todo.setPriority(todoRequest.getPriority());
            todo.setUpdatedAt(LocalDateTime.now());

            Todo updatedTodo = todoRepository.save(todo);
            return Optional.of(updatedTodo);
        }

        return Optional.empty();
    }

    // Delete todo
    public boolean deleteTodo(String id) {
        if (todoRepository.existsById(id)) {
            todoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Mark todo as completed
    public Optional<Todo> markTodoAsCompleted(String id) {
        Optional<Todo> existingTodo = todoRepository.findById(id);

        if (existingTodo.isPresent()) {
            Todo todo = existingTodo.get();
            todo.setCompleted(true);
            todo.setUpdatedAt(LocalDateTime.now());

            Todo updatedTodo = todoRepository.save(todo);
            return Optional.of(updatedTodo);
        }

        return Optional.empty();
    }

    // Mark todo as incomplete
    public Optional<Todo> markTodoAsIncomplete(String id) {
        Optional<Todo> existingTodo = todoRepository.findById(id);

        if (existingTodo.isPresent()) {
            Todo todo = existingTodo.get();
            todo.setCompleted(false);
            todo.setUpdatedAt(LocalDateTime.now());

            Todo updatedTodo = todoRepository.save(todo);
            return Optional.of(updatedTodo);
        }

        return Optional.empty();
    }

    // Get todos by completion status
    public List<Todo> getTodosByStatus(boolean completed) {
        return todoRepository.findByCompleted(completed);
    }

    // Get todos by priority
    public List<Todo> getTodosByPriority(int priority) {
        return todoRepository.findByPriority(priority);
    }

    // Search todos by title
    public List<Todo> searchTodosByTitle(String title) {
        return todoRepository.findByTitleContainingIgnoreCase(title);
    }

    // Get high priority incomplete todos
    public List<Todo> getHighPriorityIncompleteTodos() {
        return todoRepository.findHighPriorityIncompleteTodos();
    }

    // Get todos created in the last N days
    public List<Todo> getTodosCreatedInLastDays(int days) {
        LocalDateTime date = LocalDateTime.now().minusDays(days);
        return todoRepository.findTodosCreatedAfter(date);
    }

    // Get statistics
    public long getCompletedTodosCount() {
        return todoRepository.countByCompleted(true);
    }

    public long getIncompleteTodosCount() {
        return todoRepository.countByCompleted(false);
    }

    public long getTodosCountByPriority(int priority) {
        return todoRepository.countByPriority(priority);
    }
}
