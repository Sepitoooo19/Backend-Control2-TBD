package bdavanzadas.lab1.Controllers;

import bdavanzadas.lab1.entities.TaskEntity;
import bdavanzadas.lab1.services.TaskService;
import bdavanzadas.lab1.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tasks")
@Tag(name = "Gestión de Tareas", description = "Operaciones relacionadas con la gestión de tareas")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Crear una nueva tarea",
            description = "Crea una nueva tarea. Requiere autenticación.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tarea creada exitosamente",
                    content = @Content(schema = @Schema(implementation = TaskEntity.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
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

    @GetMapping("/{id}")
    @Operation(summary = "Obtener tarea por ID",
            description = "Obtiene los detalles de una tarea específica por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarea encontrada",
                    content = @Content(schema = @Schema(implementation = TaskEntity.class))),
            @ApiResponse(responseCode = "404", description = "Tarea no encontrada")
    })
    public ResponseEntity<?> getTaskById(
            @Parameter(description = "ID de la tarea a buscar", required = true)
            @PathVariable int id) {
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

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar todas las tareas",
            description = "Obtiene todas las tareas del sistema. Requiere rol ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tareas",
                    content = @Content(schema = @Schema(implementation = TaskEntity.class))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public List<TaskEntity> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/my-tasks")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Obtener mis tareas",
            description = "Obtiene las tareas del usuario autenticado.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tareas del usuario",
                    content = @Content(schema = @Schema(implementation = TaskEntity.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    public ResponseEntity<?> getMyTasks() {
        try {
            int userId = userService.getAuthenticatedUserId();
            List<TaskEntity> tasks = taskService.getTasksByUserId(userId);
            return ResponseEntity.ok(tasks);
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

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar tarea",
            description = "Actualiza una tarea existente. Solo el dueño o admin puede actualizar.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarea actualizada exitosamente",
                    content = @Content(schema = @Schema(implementation = TaskEntity.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Tarea no encontrada"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<?> updateTask(
            @Parameter(description = "ID de la tarea a actualizar", required = true)
            @PathVariable int id,
            @RequestBody TaskEntity task) {
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

    @DeleteMapping("/{id}")
    @PreAuthorize("@taskSecurity.isTaskOwnerOrAdmin(#id)")
    @Operation(summary = "Eliminar tarea",
            description = "Elimina una tarea. Solo el dueño o admin puede eliminar.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarea eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Tarea no encontrada"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<?> deleteTask(
            @Parameter(description = "ID de la tarea a eliminar", required = true)
            @PathVariable int id) {
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

    @GetMapping("/status/{status}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Filtrar tareas por estado",
            description = "Obtiene tareas filtradas por estado. Requiere autenticación.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tareas filtradas",
                    content = @Content(schema = @Schema(implementation = TaskEntity.class))),
            @ApiResponse(responseCode = "400", description = "Estado inválido"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    public ResponseEntity<?> getTasksByStatus(
            @Parameter(description = "Estado de las tareas a filtrar", required = true)
            @PathVariable String status) {
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

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or @taskSecurity.isRequestingOwnData(#userId)")
    @Operation(summary = "Filtrar tareas por usuario",
            description = "Obtiene tareas de un usuario específico. Solo admin o el propio usuario puede acceder.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tareas del usuario",
                    content = @Content(schema = @Schema(implementation = TaskEntity.class))),
            @ApiResponse(responseCode = "400", description = "ID de usuario inválido"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<?> getTasksByUserId(
            @Parameter(description = "ID del usuario cuyas tareas se quieren obtener", required = true)
            @PathVariable int userId) {
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

    @GetMapping("/filtro")
    @Operation(summary = "Filtrar tareas por estado y palabra clave",
            description = "Filtra tareas por estado y/o palabra clave en la descripción")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tareas filtradas",
                    content = @Content(schema = @Schema(implementation = TaskEntity.class)))
    })
    public List<TaskEntity> filtrarTareasPorEstadoYPalabra(
            @Parameter(description = "Estado para filtrar (opcional)")
            @RequestParam(required = false) String status,
            @Parameter(description = "Palabra clave para buscar en descripción (opcional)")
            @RequestParam(required = false) String word) {
        return taskService.filtrarTareasPorEstadoYPalabra(status, word);
    }

    @GetMapping("/tareasbysectors/{idusuario}")
    @Operation(summary = "Obtener tareas agrupadas por sector",
            description = "Obtiene las tareas de un usuario agrupadas por sector")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tareas agrupadas por sector"),

            @ApiResponse(responseCode = "400", description = "ID de usuario inválido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> getTareasBySectors(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable int idusuario) {
        try {
            return ResponseEntity.ok(taskService.getTaskbyuserBySector(idusuario));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al procesar la solicitud");
        }
    }

    @GetMapping("/mis-tareas-por-sector")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Obtener mis tareas agrupadas por sector",
            description = "Obtiene las tareas del usuario autenticado agrupadas por sector",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tareas agrupadas por sector"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "No se encontraron tareas"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> getMisTareasAgrupadasPorSector() {
        try {
            return ResponseEntity.ok(taskService.getTasksByAuthenticatedUserAndSector());
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Debe estar autenticado para acceder a este recurso");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al procesar la solicitud");
        }
    }

    @GetMapping("/tarea-pendiente-cerca")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Obtener tarea pendiente más cercana",
            description = "Obtiene la tarea pendiente más cercana al usuario autenticado",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarea pendiente más cercana"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "No se encontraron tareas pendientes"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> getTareasPendienteCerca() {
        try {
            return ResponseEntity.ok(taskService.getTareaPendienteMasCercana());
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Debe estar autenticado para acceder a este recurso");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al procesar la solicitud");
        }
    }

    @GetMapping("/sector-mas-tareas-2k")
    @Operation(summary = "Obtener sector con más tareas completadas en 2km",
            description = "Devuelve el sector con mayor cantidad de tareas completadas en un radio de 2 kilómetros")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sector encontrado",
                    content = @Content(schema = @Schema(implementation = Object.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "No se encontraron datos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> getSectorMasTareas() {
        try {
            List<Object> sector = taskService.getSectorconmastareasCompletadasEn2km();
            return ResponseEntity.ok(sector);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Debe estar autenticado para acceder a este recurso");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al procesar la solicitud");
        }
    }

    @GetMapping("/promedio-distancia-tareas-completadas")
    @Operation(summary = "Obtener distancia promedio de tareas completadas",
            description = "Calcula la distancia promedio que los usuarios han recorrido para completar tareas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Promedio calculado",
                    content = @Content(schema = @Schema(implementation = Double.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "No se encontraron datos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> getPromedioDistanciaTareasCompletadas() {
        try {
            return ResponseEntity.ok(taskService.getPromedioDistanciaTareasCompletadas());
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Debe estar autenticado para acceder a este recurso");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al procesar la solicitud");
        }
    }

    @GetMapping("/sectores-con-mas-tareas-pendientes")
    @Operation(summary = "Obtener sectores con más tareas pendientes",
            description = "Devuelve los sectores ordenados por cantidad de tareas pendientes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sectores encontrados",
                    content = @Content(schema = @Schema(implementation = Object.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "No se encontraron datos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> getTareasPorSectores() {
        try {
            return ResponseEntity.ok(taskService.getSectorsWithMostPendingTasks());
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Debe estar autenticado para acceder a este recurso");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al procesar la solicitud");
        }
    }

    @GetMapping("/tarea-pendiente-mas-cerca-allusers")
    @Operation(summary = "Obtener tarea pendiente más cercana para cualquier usuario",
            description = "Devuelve la tarea pendiente más cercana a cualquier usuario del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarea encontrada",
                    content = @Content(schema = @Schema(implementation = TaskEntity.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "No se encontraron tareas pendientes"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> getTareaPendienteMasCercanaAllUsers() {
        try {
            return ResponseEntity.ok(taskService.getTareaPendienteMasCercanaParaCualquierUsuario());
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Debe estar autenticado para acceder a este recurso");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al procesar la solicitud");
        }
    }

    @GetMapping("/tareas-usuarios-por-sector")
    @Operation(summary = "Obtener tareas de usuarios por sector",
            description = "Devuelve las tareas agrupadas por usuario y sector")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Datos encontrados",
                    content = @Content(schema = @Schema(implementation = Object.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "No se encontraron datos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> getTareasUsuariosPorSector() {
        try {
            return ResponseEntity.ok(taskService.getTareasPorUsuarioPorSector());
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Debe estar autenticado para acceder a este recurso");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al procesar la solicitud");
        }
    }

    @GetMapping("/sector-mas-tareas-5k")
    @Operation(summary = "Obtener sector con más tareas completadas en 5km",
            description = "Devuelve el sector con mayor cantidad de tareas completadas en un radio de 5 kilómetros")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sector encontrado",
                    content = @Content(schema = @Schema(implementation = Object.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "No se encontraron datos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> getSectorMasTareas5k() {
        try {
            List<Object> sector = taskService.getSectorconmastareasCompletadasEn5km();
            return ResponseEntity.ok(sector);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Debe estar autenticado para acceder a este recurso");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al procesar la solicitud");
        }
    }

    @GetMapping("/distancia-promedio-tareas-completadaas-direccion")
    @Operation(summary = "Obtener distancia promedio de tareas completadas por usuario",
            description = "Calcula la distancia promedio que el usuario autenticado ha recorrido para completar tareas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Promedio calculado",
                    content = @Content(schema = @Schema(implementation = Double.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "No se encontraron datos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> getDistanciaPromedioTareasCompletadaasDireccion() {
        try {
            return ResponseEntity.ok(taskService.getPromedioDistanciaTareasCompletadasDelUsuarioRegistrado());
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Debe estar autenticado para acceder a este recurso");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al procesar la solicitud");
        }
    }
}