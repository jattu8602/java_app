package com.example.myapp.controller;

import com.example.myapp.dto.TodoRequest;
import com.example.myapp.model.Todo;
import com.example.myapp.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "*")
public class TodoController {

    @Autowired
    private TodoService todoService;

    // CREATE - POST /api/todos
    @PostMapping
    public ResponseEntity<Todo> createTodo(@Valid @RequestBody TodoRequest todoRequest) {
        Todo createdTodo = todoService.createTodo(todoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTodo);
    }

    // READ - GET /api/todos
    @GetMapping
    public ResponseEntity<List<Todo>> getAllTodos() {
        List<Todo> todos = todoService.getAllTodos();
        return ResponseEntity.ok(todos);
    }

    // READ - GET /api/todos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable String id) {
        Optional<Todo> todo = todoService.getTodoById(id);
        return todo.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE - PUT /api/todos/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable String id,
                                         @Valid @RequestBody TodoRequest todoRequest) {
        Optional<Todo> updatedTodo = todoService.updateTodo(id, todoRequest);
        return updatedTodo.map(ResponseEntity::ok)
                         .orElse(ResponseEntity.notFound().build());
    }

    // DELETE - DELETE /api/todos/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable String id) {
        boolean deleted = todoService.deleteTodo(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // PATCH - PATCH /api/todos/{id}/complete
    @PatchMapping("/{id}/complete")
    public ResponseEntity<Todo> markTodoAsCompleted(@PathVariable String id) {
        Optional<Todo> updatedTodo = todoService.markTodoAsCompleted(id);
        return updatedTodo.map(ResponseEntity::ok)
                         .orElse(ResponseEntity.notFound().build());
    }

    // PATCH - PATCH /api/todos/{id}/incomplete
    @PatchMapping("/{id}/incomplete")
    public ResponseEntity<Todo> markTodoAsIncomplete(@PathVariable String id) {
        Optional<Todo> updatedTodo = todoService.markTodoAsIncomplete(id);
        return updatedTodo.map(ResponseEntity::ok)
                         .orElse(ResponseEntity.notFound().build());
    }

    // READ - GET /api/todos/status/{completed}
    @GetMapping("/status/{completed}")
    public ResponseEntity<List<Todo>> getTodosByStatus(@PathVariable boolean completed) {
        List<Todo> todos = todoService.getTodosByStatus(completed);
        return ResponseEntity.ok(todos);
    }

    // READ - GET /api/todos/priority/{priority}
    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<Todo>> getTodosByPriority(@PathVariable int priority) {
        if (priority < 1 || priority > 3) {
            return ResponseEntity.badRequest().build();
        }
        List<Todo> todos = todoService.getTodosByPriority(priority);
        return ResponseEntity.ok(todos);
    }

    // READ - GET /api/todos/search?title={title}
    @GetMapping("/search")
    public ResponseEntity<List<Todo>> searchTodosByTitle(@RequestParam String title) {
        List<Todo> todos = todoService.searchTodosByTitle(title);
        return ResponseEntity.ok(todos);
    }

    // READ - GET /api/todos/high-priority/incomplete
    @GetMapping("/high-priority/incomplete")
    public ResponseEntity<List<Todo>> getHighPriorityIncompleteTodos() {
        List<Todo> todos = todoService.getHighPriorityIncompleteTodos();
        return ResponseEntity.ok(todos);
    }

    // READ - GET /api/todos/recent/{days}
    @GetMapping("/recent/{days}")
    public ResponseEntity<List<Todo>> getRecentTodos(@PathVariable int days) {
        if (days < 1 || days > 365) {
            return ResponseEntity.badRequest().build();
        }
        List<Todo> todos = todoService.getTodosCreatedInLastDays(days);
        return ResponseEntity.ok(todos);
    }

    // READ - GET /api/todos/stats
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getTodoStats() {
        Map<String, Object> stats = Map.of(
            "totalCompleted", todoService.getCompletedTodosCount(),
            "totalIncomplete", todoService.getIncompleteTodosCount(),
            "lowPriority", todoService.getTodosCountByPriority(1),
            "mediumPriority", todoService.getTodosCountByPriority(2),
            "highPriority", todoService.getTodosCountByPriority(3)
        );
        return ResponseEntity.ok(stats);
    }

    // Health check endpoint
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        return ResponseEntity.ok(Map.of("status", "UP", "message", "Todo API is running"));
    }
}
