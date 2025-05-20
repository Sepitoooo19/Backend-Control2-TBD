package bdavanzadas.lab1.services;

import bdavanzadas.lab1.entities.TaskEntity;
import bdavanzadas.lab1.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import bdavanzadas.lab1.repositories.SectorRepository;
import java.util.stream.Collectors;
import bdavanzadas.lab1.repositories.UserRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private SectorRepository sectorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    public TaskEntity createTask(TaskEntity task) {
        // 1. Validar usuario autenticado
        int userId = userService.getAuthenticatedUserId();
        task.setUserId(userId);

        // 2. Validaciones básicas
        if (task.getTitle() == null || task.getTitle().isEmpty()) {
            throw new IllegalArgumentException("El título de la tarea no puede estar vacío");
        }

        if (task.getDueDate() != null && task.getDueDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha de vencimiento no puede ser anterior a la fecha actual");
        }

        // 3. Validar sector existe
        if (task.getSectorId() == 0 || !sectorRepository.findById(task.getSectorId()).isPresent()) {
            throw new IllegalArgumentException("El sector especificado no existe");
        }

        // 4. Validar ubicación geográfica
        if (task.getLocation() == null || !task.getLocation().startsWith("POINT(")) {
            throw new IllegalArgumentException("Formato de ubicación inválido. Use: POINT(longitud latitud)");
        }

        // 5. Establecer valores por defecto
        if (task.getStatus() == null) {
            task.setStatus("PENDING");
        }

        // 6. Guardar la tarea
        return taskRepository.save(task);
    }


    // Obtener tarea por ID
    public TaskEntity getTaskById(int id) {
        TaskEntity task = taskRepository.findById(id);
        if (task == null) {
            throw new RuntimeException("Tarea no encontrada con ID: " + id);
        }
        return task;
    }

    // Obtener todas las tareas
    public List<TaskEntity> getAllTasks() {
        return taskRepository.findAll();
    }

    // Actualizar una tarea existente
    public TaskEntity updateTask(TaskEntity task) {
        if (task.getId() <= 0) {
            throw new IllegalArgumentException("ID de tarea inválido");
        }
        if (!taskRepository.update(task)) {
            throw new RuntimeException("No se pudo actualizar la tarea con ID: " + task.getId());
        }
        return taskRepository.findById(task.getId());
    }

    // Eliminar una tarea por ID
    public boolean deleteTask(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de tarea inválido");
        }
        if (!taskRepository.deleteById(id)) {
            throw new RuntimeException("No se pudo eliminar la tarea con ID: " + id);
        }
        return true;
    }

    // Obtener tareas por estado (Ejemplo adicional)
    public List<TaskEntity> getTasksByStatus(String status) {
        if (status == null || status.isEmpty()) {
            throw new IllegalArgumentException("El estado no puede estar vacío");
        }
        return taskRepository.findAll().stream()
                .filter(task -> task.getStatus().equalsIgnoreCase(status))
                .toList();
    }

    // Obtener tareas por usuario (Ejemplo adicional)
    public List<TaskEntity> getTasksByUserId(int userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("ID de usuario inválido");
        }
        return taskRepository.findAll().stream()
                .filter(task -> task.getUserId() == userId)
                .toList();
    }

    //filtro de tareas
    public List<TaskEntity> filtrarTareasPorEstadoYPalabra(String status, String palabra) {
        List<TaskEntity> tareasAFiltrar;

        // 1. Determinar la lista inicial de tareas según el estado
        if (status == null || status.trim().isEmpty()) {
            // Si no se proporciona un estado, obtenemos todas las tareas
            tareasAFiltrar = getAllTasks();
        } else {
            // Si se proporciona un estado, filtramos por ese estado
            tareasAFiltrar = getTasksByStatus(status);
        }

        if (tareasAFiltrar == null) {
            return List.of();
        }

        // 2. Filtrar por palabra clave (si se proporciona)
        if (palabra == null || palabra.trim().isEmpty()) {
            return tareasAFiltrar;
        }

        String palabraEnMinusculas = palabra.toLowerCase().trim();

        return tareasAFiltrar.stream()
                .filter(tarea -> {
                    boolean coincideTitulo = false;
                    if (tarea.getTitle() != null) {
                        coincideTitulo = tarea.getTitle().toLowerCase().contains(palabraEnMinusculas);
                    }

                    boolean coincideDescripcion = false;
                    if (tarea.getDescription() != null) {
                        coincideDescripcion = tarea.getDescription().toLowerCase().contains(palabraEnMinusculas);
                    }

                    // La tarea se incluye si la palabra clave coincide con el título O la descripción
                    return coincideTitulo || coincideDescripcion;
                })
                .collect(Collectors.toList()); // Recolectamos los resultados en una nueva lista
    }
}