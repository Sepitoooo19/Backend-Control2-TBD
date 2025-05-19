package bdavanzadas.lab1.Controllers;

import bdavanzadas.lab1.entities.TaskEntity;
import bdavanzadas.lab1.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    // Crear tarea (solo usuarios autenticados)
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createTask(@RequestBody TaskEntity task) {
        try {
            TaskEntity createdTask = taskService.createTask(task);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    // Obtener tarea por ID (accesible para todos)
    @GetMapping("/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable int id) {
        try {
            TaskEntity task = taskService.getTaskById(id);
            return ResponseEntity.ok(task);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    // Listar todas las tareas (solo admin)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<TaskEntity> getAllTasks() {
        return taskService.getAllTasks();
    }

    // Actualizar tarea (solo el dueño o admin)
    @PutMapping("/{id}")
    @PreAuthorize("@taskSecurity.isTaskOwnerOrAdmin(#id)")
    public ResponseEntity<?> updateTask(@PathVariable int id, @RequestBody TaskEntity task) {
        try {
            task.setId(id);
            TaskEntity updatedTask = taskService.updateTask(task);
            return ResponseEntity.ok(updatedTask);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    // Eliminar tarea (solo el dueño o admin)
    @DeleteMapping("/{id}")
    @PreAuthorize("@taskSecurity.isTaskOwnerOrAdmin(#id)")
    public ResponseEntity<?> deleteTask(@PathVariable int id) {
        try {
            boolean deleted = taskService.deleteTask(id);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Tarea eliminada correctamente"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    // Filtrar por estado (accesible para usuarios autenticados)
    @GetMapping("/status/{status}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getTasksByStatus(@PathVariable String status) {
        try {
            List<TaskEntity> tasks = taskService.getTasksByStatus(status);
            return ResponseEntity.ok(tasks);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    // Filtrar por usuario (solo admin o el propio usuario)
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or @taskSecurity.isRequestingOwnData(#userId)")
    public ResponseEntity<?> getTasksByUserId(@PathVariable int userId) {
        try {
            List<TaskEntity> tasks = taskService.getTasksByUserId(userId);
            return ResponseEntity.ok(tasks);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
}