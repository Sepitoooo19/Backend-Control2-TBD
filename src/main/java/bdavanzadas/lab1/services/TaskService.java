package bdavanzadas.lab1.services;

import bdavanzadas.lab1.entities.SectorEntity;
import bdavanzadas.lab1.entities.TaskEntity;
import bdavanzadas.lab1.entities.UserEntity;
import bdavanzadas.lab1.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import bdavanzadas.lab1.repositories.SectorRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import bdavanzadas.lab1.repositories.UserRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

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

    /**
     * Crea una nueva tarea con validaciones previas.
     * @param task La tarea a crear.
     * @return La tarea creada con su ID asignado.
     * @throws IllegalArgumentException Si los datos de la tarea no son válidos.
     */
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

    /**
     * Obtiene una tarea por su ID.
     * @param id El ID de la tarea a buscar.
     * @return La tarea encontrada.
     * @throws RuntimeException Si no se encuentra la tarea.
     */
    public TaskEntity getTaskById(int id) {
        TaskEntity task = taskRepository.findById(id);
        if (task == null) {
            throw new RuntimeException("Tarea no encontrada con ID: " + id);
        }
        return task;
    }

    /**
     * Obtiene todas las tareas existentes.
     * @return Lista de todas las tareas.
     */
    public List<TaskEntity> getAllTasks() {
        return taskRepository.findAll();
    }

    /**
     * Actualiza una tarea existente.
     * @param task La tarea con los datos actualizados.
     * @return La tarea actualizada.
     * @throws IllegalArgumentException Si el ID de la tarea es inválido.
     * @throws RuntimeException Si no se encuentra la tarea o no se puede actualizar.
     */
    public TaskEntity updateTask(TaskEntity task) {
        System.out.println("Actualizando tarea con ID: " + task.getId()); // Debug

        if (task.getId() <= 0) {
            throw new IllegalArgumentException("ID de tarea inválido");
        }

        // Verificar que la tarea existe antes de actualizar
        TaskEntity existingTask = taskRepository.findById(task.getId());
        if (existingTask == null) {
            throw new RuntimeException("No se encontró la tarea con ID: " + task.getId());
        }

        System.out.println("Tarea encontrada, procediendo a actualizar..."); // Debug

        if (!taskRepository.update(task)) {
            throw new RuntimeException("No se pudo actualizar la tarea con ID: " + task.getId());
        }

        System.out.println("Tarea actualizada exitosamente"); // Debug
        return taskRepository.findById(task.getId());
    }

    /**
     * Elimina una tarea por su ID.
     * @param id El ID de la tarea a eliminar.
     * @return true si la eliminación fue exitosa.
     * @throws IllegalArgumentException Si el ID de la tarea es inválido.
     * @throws RuntimeException Si no se puede eliminar la tarea.
     */
    public boolean deleteTask(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de tarea inválido");
        }
        if (!taskRepository.deleteById(id)) {
            throw new RuntimeException("No se pudo eliminar la tarea con ID: " + id);
        }
        return true;
    }

    /**
     * Obtiene tareas filtradas por estado.
     * @param status El estado por el que filtrar (ej. "PENDING", "COMPLETED").
     * @return Lista de tareas que coinciden con el estado.
     * @throws IllegalArgumentException Si el estado es nulo o vacío.
     */
    public List<TaskEntity> getTasksByStatus(String status) {
        if (status == null || status.isEmpty()) {
            throw new IllegalArgumentException("El estado no puede estar vacío");
        }
        return taskRepository.findAll().stream()
                .filter(task -> task.getStatus().equalsIgnoreCase(status))
                .toList();
    }

    /**
     * Obtiene tareas filtradas por ID de usuario.
     * @param userId El ID del usuario cuyas tareas se quieren obtener.
     * @return Lista de tareas asignadas al usuario.
     * @throws IllegalArgumentException Si el ID de usuario es inválido.
     */
    public List<TaskEntity> getTasksByUserId(int userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("ID de usuario inválido");
        }
        return taskRepository.findAll().stream()
                .filter(task -> task.getUserId() == userId)
                .toList();
    }

    //filtro de tareas
    /**
     * Filtra las tareas de un usuario por estado y palabra clave.
     * Si no se proporciona un estado, se obtienen todas las tareas del usuario.
     * Si no se proporciona una palabra clave, se devuelven las tareas filtradas por estado.
     * si ambos parámetros son nulos o vacíos, se devuelven todas las tareas del usuario.
     *
     * @param status  El estado de las tareas a filtrar (por ejemplo, "PENDING", "COMPLETED").
     * @param palabra La palabra clave para filtrar por título o descripción de la tarea.
     * @return Una lista de TaskEntity que cumplen con los criterios de filtrado.
     */
    public List<TaskEntity> filtrarTareasPorEstadoYPalabra(String status, String palabra) {
        List<TaskEntity> tareasAFiltrar;
        int userId = userService.getAuthenticatedUserId();
        // 1. Determinar la lista inicial de tareas según el estado
        if (status == null || status.trim().isEmpty()) {
            // Si no se proporciona un estado, obtenemos todas las tareas
            tareasAFiltrar = getTasksByUserId(userId);
        } else {
            // Si se proporciona un estado, filtramos por ese estado
            tareasAFiltrar = taskRepository.findByStatusUser(userId, status);
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

    /**
     * Obtiene la ubicación del usuario autenticado en formato WKT.
     * @return La ubicación en formato WKT.
     */
    private String getAuthenticatedUserLocationWKT() {
        int userId = userService.getAuthenticatedUserId();
        String location = userService.getAuthenticatedUserLocation();
        return location;
    }

    /**
     * Valida que la ubicación exista.
     * @param locationWKT La ubicación a validar.
     * @throws RuntimeException Si la ubicación es nula o vacía.
     */
    private void validateLocationExists(String locationWKT) {
        if (locationWKT == null || locationWKT.trim().isEmpty()) {
            throw new RuntimeException("El usuario no tiene ubicación registrada");
        }
    }

    // REQUERIMIENTOS FUNCIONALES

    // 1- TAREAS DEL USUARIO POR SECTOR
    /**
     * Obtiene las tareas de un usuario específico y las agrupa por sector,
     * contando cuántas tareas pertenecen a cada sector para ese usuario.
     *
     * @param userId ID del usuario.
     * @return Una lista de listas de objetos. Cada lista interna contiene dos elementos:
     * el nombre del sector (String) y la cantidad de tareas (Long) en ese sector
     * para el usuario especificado.
     * Ejemplo: [ ["NombreSector1", 5], ["NombreSector2", 3] ]
     *
     */
    public List<List<Object>> getTaskbyuserBySector(int userId) {
        List<Map<String, Object>> results = taskRepository.countTasksByUserAndSector(userId);

        return results.stream()
                .map(row -> {
                    List<Object> entry = new ArrayList<>();
                    entry.add(row.get("sector_name"));
                    entry.add(row.get("task_count"));
                    return entry;
                })
                .collect(Collectors.toList());
    }

    public Map<String, Object> getTaskDistributionByUserAndSector() {
        List<Map<String, Object>> rawData = taskRepository.countTasksByAllUsersAndSectors();

        // Get all unique sector names
        Set<String> sectors = rawData.stream()
                .map(row -> (String) row.get("sector_name"))
                .collect(Collectors.toCollection(TreeSet::new)); // Sorted

        // Group by user
        Map<String, Map<String, Long>> userData = rawData.stream()
                .collect(Collectors.groupingBy(
                        row -> (String) row.get("user_name"),
                        Collectors.toMap(
                                row -> (String) row.get("sector_name"),
                                row -> ((Number) row.get("task_count")).longValue()
                        )
                ));

        // Ensure all sectors are present for each user with 0 if missing
        userData.forEach((user, sectorCounts) -> {
            sectors.forEach(sector -> sectorCounts.putIfAbsent(sector, 0L));
        });

        Map<String, Object> result = new HashMap<>();
        result.put("sectors", new ArrayList<>(sectors));
        result.put("userData", userData);

        return result;
    }

    public List<Map<String, Object>> getTasksByAuthenticatedUserAndSector() {
        // 1. Obtener el ID del usuario autenticado
        int userId = userService.getAuthenticatedUserId();

        // 3. Usar el método del repositorio que ya tenemos
        return taskRepository.countTasksByUserAndSector(userId);
    }

    //2- tarea pendiente mas cercana al usuario
    /**
     * Obtiene la tarea pendiente más cercana al usuario autenticado
     * según su ubicación geográfica.
     *
     *
     *
     * @return La tarea pendiente más cercana al usuario.
     */
    public TaskEntity getTareaPendienteMasCercana() {
        int userId = userService.getAuthenticatedUserId();
        String userLocationWKT = getAuthenticatedUserLocationWKT();
        validateLocationExists(userLocationWKT);
        return taskRepository.findNearestPendingTaskForUser(userId, userLocationWKT);
    }

    //3- sector con mas tareas completadas en un radio de 2km
    /**
     * Obtiene el sector con más tareas completadas en un radio de 2 km
     * alrededor de la ubicación del usuario autenticado.
     *
     *
     * @return El sector con más tareas completadas en el radio especificado.
     */
    public List<Object> getSectorconmastareasCompletadasEn2km() {
        int userId = userService.getAuthenticatedUserId();
        String locationWKT = getAuthenticatedUserLocationWKT();
        validateLocationExists(locationWKT);
        return taskRepository.findSectorWithMostCompletedTasksInRadius(userId, locationWKT, 2000);
    }



    //4 ¿Cuál es el promedio de distancia de las tareas completadas respecto a la ubicación del usuario?
    /**
     * Obtiene el promedio de distancia de las tareas completadas
     * respecto a la ubicación del usuario autenticado.
     *
     *
     * @return El promedio de distancia de las tareas completadas respecto a la ubicación del usuario.
     */
    public Float getPromedioDistanciaTareasCompletadas() {
        int userId = userService.getAuthenticatedUserId();
        String locationWKT = getAuthenticatedUserLocationWKT();
        validateLocationExists(locationWKT);

        Float averageDistance = taskRepository.findAverageDistanceOfCompletedTasks(userId, locationWKT);

        if (averageDistance == null) {
            throw new RuntimeException("No se encontraron tareas completadas para el usuario");
        }

        return averageDistance;
    }

    //5- en que sectores geograficos se concentran la mayoria de pendientes
    /**
     * Obtiene los sectores geográficos donde se concentran la mayoría de las tareas pendientes
     * para el usuario autenticado.
     *
     * @return Una lista de entidades SectorEntity que representan los sectores con más tareas pendientes.
     */
    public List<SectorEntity> getSectorsWithMostPendingTasks() {
        int userId = userService.getAuthenticatedUserId();
        List<SectorEntity> sectors = taskRepository.findSectorsWithMostPendingTasks(userId);

        if (sectors.isEmpty()) {
            throw new RuntimeException("No se encontraron sectores con tareas pendientes");
        }

        return sectors;
    }

    // 6- ¿Cuál es la tarea pendiente más cercana a la ubicación del usuario? (de cualquier usuario)
    /**
     * Obtiene la tarea pendiente más cercana a la ubicación del usuario,
     * sin importar el usuario al que pertenezca la tarea.
     *
     * @return La tarea pendiente más cercana a la ubicación proporcionada.
     */
    public TaskEntity getTareaPendienteMasCercanaParaCualquierUsuario() {
        String userLocationWKT = getAuthenticatedUserLocationWKT();
        validateLocationExists(userLocationWKT);

        TaskEntity nearestPendingTask = taskRepository.findNearestPendingTask(userLocationWKT);

        if (nearestPendingTask == null) {
            throw new RuntimeException("No se encontraron tareas pendientes cercanas");
        }

        return nearestPendingTask;
    }

    // 7- ¿Cuántas tareas ha realizado cada usuario por sector?
    /**
     * Obtiene el conteo de tareas realizadas por cada usuario, agrupadas por sector.
     *
     * @return Una lista de mapas, donde cada mapa contiene el ID del usuario, el nombre del sector
     * y la cantidad de tareas realizadas por ese usuario en ese sector.
     */
    public List<List<Object>> getTareasPorUsuarioPorSector() {
        return taskRepository.getTaskCountSector();
    }

    // 8- Cuál es el sector con más tareas completadas dentro de un radio de 5 km
    //desde la ubicación del usuario?
    /**
     * Obtiene el sector con más tareas completadas en un radio de 5 km
     * alrededor de la ubicación del usuario autenticado.
     *
      * @return Una lista de objetos que representan el sector con más tareas completadas en el radio especificado.
     */
    public List<Object> getSectorconmastareasCompletadasEn5km() {
        int userId = userService.getAuthenticatedUserId();
        String locationWKT = getAuthenticatedUserLocationWKT();
        validateLocationExists(locationWKT);
        return taskRepository.findSectorWithMostCompletedTasksInRadius5km(userId, locationWKT, 5000);
    }

    // 9-¿Cuál es el promedio de distancia entre las tareas completadas y el punto
    //registrado del usuario?
    /**
     * Obtiene el promedio de distancia de las tareas completadas
     * respecto a la ubicación registrada del usuario autenticado.
     *
     * @return El promedio de distancia de las tareas completadas respecto a la ubicación del usuario.
     * @throws RuntimeException si el usuario no tiene una ubicación registrada válida o si no hay tareas completadas.
     */
    public Float getPromedioDistanciaTareasCompletadasDelUsuarioRegistrado() {
        // Este método ya usa la ubicación registrada, no necesita cambios
        int userId = userService.getAuthenticatedUserId();
        UserEntity authenticatedUser = userRepository.findById(userId);
        if (authenticatedUser == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        String userRegisteredLocationWKT = authenticatedUser.getLocation();
        if (userRegisteredLocationWKT == null || userRegisteredLocationWKT.trim().isEmpty()) {
            throw new RuntimeException("El usuario no tiene ubicación registrada válida");
        }

        Float averageDistance = taskRepository.findAverageDistanceOfCompletedTasks(
                userId,
                userRegisteredLocationWKT
        );

        if (averageDistance == null) {
            throw new RuntimeException("No se encontraron tareas completadas para el usuario");
        }

        return averageDistance;
    }


}