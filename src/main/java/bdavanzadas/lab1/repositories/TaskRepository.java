package bdavanzadas.lab1.repositories;

import bdavanzadas.lab1.entities.SectorEntity;
import bdavanzadas.lab1.entities.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * La clase TaskRepository representa el repositorio de tareas en la base de datos.
 * Esta clase contiene métodos para realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * sobre la tabla de tareas, así como consultas especializadas que incluyen operaciones geográficas.
 * Utiliza JdbcTemplate de Spring para la interacción con la base de datos.
 */
@Repository
public class TaskRepository implements TaskRepositoryInt {

    /**
     * JdbcTemplate es una clase de Spring que simplifica el acceso a la base de datos.
     * Se utiliza para ejecutar consultas SQL y mapear los resultados a objetos Java.
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Método para guardar una nueva tarea en la base de datos.
     * @param "task" El objeto TaskEntity que representa la tarea a guardar.
     * @return La misma tarea con el ID y fecha de creación asignados.
     * La ubicación debe estar en formato WKT (Well-Known Text).
     * La fecha de creación se asigna automáticamente en la base de datos.
     */
    public TaskEntity save(TaskEntity task) {
        String sql = """
        INSERT INTO tasks 
            (title, description, due_date, status, user_id, sector_id, location) 
        VALUES 
            (?, ?, ?, ?, ?, ?, ST_GeomFromText(?, 4326))
        RETURNING id, created_at
        """;

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                    task.setId(rs.getInt("id"));
                    task.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
                    return task;
                },
                task.getTitle(),
                task.getDescription(),
                task.getDueDate(),
                task.getStatus(),
                task.getUserId(),
                task.getSectorId(),
                task.getLocation());
    }

    /**
     * Método para encontrar una tarea por su ID.
     * @param "id" El ID de la tarea a buscar.
     * @return El objeto TaskEntity correspondiente al ID proporcionado, o null si no se encuentra.
     * La ubicación se devuelve en formato WKT (Well-Known Text).
     */
    public TaskEntity findById(int id) {
        String sql = "SELECT id, title, description, due_date, status, user_id, sector_id, " +
                "ST_AsText(location) as location, created_at FROM tasks WHERE id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
                TaskEntity task = new TaskEntity();
                task.setId(rs.getInt("id"));
                task.setTitle(rs.getString("title"));
                task.setDescription(rs.getString("description"));
                task.setDueDate(rs.getTimestamp("due_date") != null ?
                        rs.getTimestamp("due_date").toLocalDateTime() : null);
                task.setStatus(rs.getString("status"));
                task.setUserId(rs.getInt("user_id"));
                task.setSectorId(rs.getInt("sector_id"));
                task.setLocation(rs.getString("location"));
                task.setCreatedAt(rs.getTimestamp("created_at") != null ?
                        rs.getTimestamp("created_at").toLocalDateTime() : null);
                return task;
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * Método para obtener todas las tareas registradas en el sistema.
     * @return Una lista de objetos TaskEntity con todas las tareas.
     * La ubicación de cada tarea se devuelve en formato WKT (Well-Known Text).
     */
    public List<TaskEntity> findAll() {
        String sql = """
            SELECT 
                id, title, description, due_date, status, 
                user_id, sector_id, ST_AsText(location) as location, created_at
            FROM tasks
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new TaskEntity(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getObject("due_date", LocalDateTime.class),
                        rs.getString("status"),
                        rs.getInt("user_id"),
                        rs.getInt("sector_id"),
                        rs.getString("location"),
                        rs.getObject("created_at", LocalDateTime.class)
                ));
    }

    /**
     * Método para actualizar una tarea existente.
     * @param "task" El objeto TaskEntity con los datos actualizados.
     * @return true si la actualización fue exitosa, false si no se encontró la tarea.
     * La ubicación debe estar en formato WKT (Well-Known Text).
     */
    public boolean update(TaskEntity task) {
        String sql = "UPDATE tasks SET title = ?, description = ?, due_date = ?, status = ?, " +
                "sector_id = ?, location = ST_GeomFromText(?, 4326) WHERE id = ?";

        try {
            int rowsAffected = jdbcTemplate.update(sql,
                    task.getTitle(),
                    task.getDescription(),
                    task.getDueDate(),
                    task.getStatus(),
                    task.getSectorId(),
                    task.getLocation(),
                    task.getId()
            );
            return rowsAffected > 0;
        } catch (Exception e) {
            System.err.println("Error actualizando tarea: " + e.getMessage());
            return false;
        }
    }

    /**
     * Método para eliminar una tarea por su ID.
     * @param "id" El ID de la tarea a eliminar.
     * @return true si se eliminó correctamente, false si no se encontró la tarea.
     */
    public boolean deleteById(int id) {
        String sql = "DELETE FROM tasks WHERE id = ?";
        int deleted = jdbcTemplate.update(sql, id);
        return deleted > 0;
    }

    /**
     * Método para filtrar tareas por estado y usuario.
     * @param "userId" El ID del usuario cuyas tareas se quieren filtrar.
     * @param "status" El estado por el que filtrar (ej. 'PENDING', 'COMPLETED').
     * @return Lista de tareas que cumplen con los criterios de filtrado.
     */
    public List<TaskEntity> findByStatusUser(int userId, String status) {
        String sql = """
        SELECT 
            id, title, description, due_date, status, user_id, sector_id, 
            ST_AsText(location) as location, created_at
        FROM 
            tasks
        WHERE 
            user_id = ? AND status = ?
        """;

        return jdbcTemplate.query(sql, new Object[]{userId, status}, (rs, rowNum) -> {
            TaskEntity task = new TaskEntity();
            task.setId(rs.getInt("id"));
            task.setTitle(rs.getString("title"));
            task.setDescription(rs.getString("description"));
            task.setDueDate(rs.getTimestamp("due_date") != null ?
                    rs.getTimestamp("due_date").toLocalDateTime() : null);
            task.setStatus(rs.getString("status"));
            task.setUserId(rs.getInt("user_id"));
            task.setSectorId(rs.getInt("sector_id"));
            task.setLocation(rs.getString("location"));
            task.setCreatedAt(rs.getTimestamp("created_at") != null ?
                    rs.getTimestamp("created_at").toLocalDateTime() : null);
            return task;
        });
    }

    /**
     * Método para contar tareas por usuario y sector.
     * @param "userId" El ID del usuario cuyas tareas se quieren contar.
     * @return Lista de mapas con el nombre del sector y el conteo de tareas.
     */
    public List<Map<String, Object>> countTasksByUserAndSector(int userId) {
        String sql = """
        SELECT 
            s.name AS sector_name, 
            COUNT(t.id) AS task_count
        FROM 
            tasks t
        JOIN 
            sectors s ON t.sector_id = s.id
        WHERE 
            t.user_id = ?
        GROUP BY 
            s.name
        ORDER BY 
            task_count DESC
        """;

        return jdbcTemplate.queryForList(sql, userId);
    }

<<<<<<< Updated upstream
    /**
     * Método para encontrar la tarea pendiente más cercana a un usuario.
     * @param "userId" El ID del usuario para el que se busca la tarea.
     * @param "userLocationWKT" La ubicación del usuario en formato WKT.
     * @return La tarea pendiente más cercana, o null si no hay tareas pendientes.
     */
    public TaskEntity findNearestPendingTaskForUser(int userId, String userLocationWKT) {
        String sql = "SELECT id, title, description, due_date, status, user_id, sector_id, " +
                "       ST_AsText(location) as location_wkt, created_at " +
                "FROM tasks " +
                "WHERE status = 'PENDING' AND user_id = ? " +
                "ORDER BY ST_Distance(ST_GeomFromText(?, 4326), location) " +
                "LIMIT 1";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{userId, userLocationWKT}, (rs, rowNum) -> {
                TaskEntity task = new TaskEntity();
                task.setId(rs.getInt("id"));
                task.setTitle(rs.getString("title"));
                task.setDescription(rs.getString("description"));
                task.setDueDate(rs.getTimestamp("due_date") != null ?
                        rs.getTimestamp("due_date").toLocalDateTime() : null);
                task.setStatus(rs.getString("status"));
                task.setUserId(rs.getInt("user_id"));
                task.setSectorId(rs.getInt("sector_id"));
                task.setLocation(rs.getString("location_wkt"));
                task.setCreatedAt(rs.getTimestamp("created_at") != null ?
                        rs.getTimestamp("created_at").toLocalDateTime() : null);
                return task;
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * Método para encontrar el sector con más tareas completadas en un radio determinado.
     * @param "userId" El ID del usuario cuyas tareas se consideran.
     * @param "locationWKT" La ubicación central en formato WKT.
     * @param "radius" El radio en metros alrededor de la ubicación central.
     * @return Lista con el nombre del sector y el conteo de tareas, o lista vacía si no hay resultados.
     */
    public List<Object> findSectorWithMostCompletedTasksInRadius(int userId, String locationWKT, int radius) {
        String sql = """
            SELECT
                s.name AS sector_name,
                COUNT(t.id) AS completed_task_count
            FROM
                tasks t
            JOIN
                sectors s ON t.sector_id = s.id
            WHERE
                t.user_id = ?         
                AND t.status = 'COMPLETED'
                AND ST_DWithin(
                        t.location::geography,                
                        ST_GeomFromText(?, 4326)::geography,  
                        ?                                     
                    )
            GROUP BY
                s.name
            ORDER BY
                completed_task_count DESC
            LIMIT 1
            """;

        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{userId, locationWKT, radius}, (rs, rowNum) -> {
                List<Object> result = new ArrayList<>();
                result.add(rs.getString("sector_name"));
                result.add(rs.getLong("completed_task_count"));
                return result;
            });
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    /**
     * Método para calcular la distancia promedio de las tareas completadas respecto a una ubicación.
     * @param "userId" El ID del usuario cuyas tareas se consideran.
     * @param "locationWKT" La ubicación de referencia en formato WKT.
     * @return La distancia promedio en metros, o null si no hay tareas completadas.
     */
    public Float findAverageDistanceOfCompletedTasks(int userId,String locationWKT){
        String sql = """
        SELECT AVG(ST_Distance(ST_GeomFromText(?, 4326), t.location::geography)) AS average_distance
        FROM tasks t
        WHERE t.user_id = ? AND t.status = 'COMPLETED'
        """;

        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{locationWKT, userId}, Float.class);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * Método para encontrar los sectores con más tareas pendientes.
     * @param "userId" El ID del usuario cuyas tareas se consideran.
     * @return Lista de hasta 3 sectores ordenados por cantidad de tareas pendientes.
     */
    public List<SectorEntity> findSectorsWithMostPendingTasks(int userId) {
        String sql = """
        SELECT
            s.id AS sector_id,
            s.name AS sector_name,
            ST_AsText(s.location) AS sector_location,
            COUNT(t.id) AS pending_task_count
        FROM
            tasks t
        JOIN
            sectors s ON t.sector_id = s.id
        WHERE
            t.user_id = ?
            AND t.status = 'PENDING'
        GROUP BY
            s.id, s.name, s.location
        ORDER BY
            pending_task_count DESC
        LIMIT 3
        """;

        RowMapper<SectorEntity> rowMapper = (rs, rowNum) -> {
            SectorEntity sector = new SectorEntity();
            sector.setId(rs.getInt("sector_id"));
            sector.setName(rs.getString("sector_name"));
            sector.setLocation(rs.getString("sector_location"));
            return sector;
        };

        return jdbcTemplate.query(sql, new Object[]{userId}, rowMapper);
    }

    /**
     * Método para encontrar la tarea pendiente más cercana a una ubicación (de cualquier usuario).
     * @param "locationWKT" La ubicación de referencia en formato WKT.
     * @return La tarea pendiente más cercana, o null si no hay tareas pendientes.
     */
    public TaskEntity findNearestPendingTask(String locationWKT) {
        String sql = """
        SELECT id, title, description, due_date, status, user_id, sector_id, 
               ST_AsText(location) as location_wkt, created_at
        FROM tasks
        WHERE status = 'PENDING'
        ORDER BY ST_Distance(ST_GeomFromText(?, 4326), location) 
        LIMIT 1
        """;

        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{locationWKT}, (rs, rowNum) -> {
                TaskEntity task = new TaskEntity();
                task.setId(rs.getInt("id"));
                task.setTitle(rs.getString("title"));
                task.setDescription(rs.getString("description"));
                task.setDueDate(rs.getTimestamp("due_date") != null ?
                        rs.getTimestamp("due_date").toLocalDateTime() : null);
                task.setStatus(rs.getString("status"));
                task.setUserId(rs.getInt("user_id"));
                task.setSectorId(rs.getInt("sector_id"));
                task.setLocation(rs.getString("location_wkt"));
                task.setCreatedAt(rs.getTimestamp("created_at") != null ?
                        rs.getTimestamp("created_at").toLocalDateTime() : null);
                return task;
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * Método para obtener el conteo de tareas realizadas por usuario y sector.
     * @return Lista de listas con nombre de usuario, nombre de sector y conteo de tareas.
     */
    public List<List<Object>> getTaskCountSector() {
        String sql = """
        SELECT 
            u.username AS user_name, 
            s.name AS sector_name, 
            COUNT(t.id) AS task_count
        FROM 
            tasks t
        JOIN 
            users u ON t.user_id = u.id
        JOIN 
            sectors s ON t.sector_id = s.id
        GROUP BY 
            u.username, s.name
        ORDER BY 
            u.username, s.name
        """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            List<Object> row = new ArrayList<>();
            row.add(rs.getString("user_name"));
            row.add(rs.getString("sector_name"));
            row.add(rs.getInt("task_count"));
            return row;
        });
    }

    /**
     * Método para encontrar el sector con más tareas completadas en un radio de 5km.
     * @param "userId" El ID del usuario cuyas tareas se consideran.
     * @param "locationWKT" La ubicación central en formato WKT.
     * @param "radius" El radio en metros (5000 para 5km).
     * @return Lista con el nombre del sector y el conteo de tareas, o lista vacía si no hay resultados.
     */
    public List<Object> findSectorWithMostCompletedTasksInRadius5km(int userId, String locationWKT, int radius) {
        String sql = """
            SELECT
                s.name AS sector_name,
                COUNT(t.id) AS completed_task_count
            FROM
                tasks t
            JOIN
                sectors s ON t.sector_id = s.id
            WHERE
                t.user_id = ?         
                AND t.status = 'COMPLETED'
                AND ST_DWithin(
                        t.location::geography,                
                        ST_GeomFromText(?, 4326)::geography,  
                        ?                                     
                    )
            GROUP BY
                s.name
            ORDER BY
                completed_task_count DESC
            LIMIT 1
            """;

        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{userId, locationWKT, radius}, (rs, rowNum) -> {
                List<Object> result = new ArrayList<>();
                result.add(rs.getString("sector_name"));
                result.add(rs.getLong("completed_task_count"));
                return result;
            });
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    /**
     * Método para calcular la distancia promedio de las tareas completadas respecto a una ubicación.
     * @param "userId" El ID del usuario cuyas tareas se consideran.
     * @param "userLocationWkt" La ubicación de referencia en formato WKT.
     * @return La distancia promedio en metros, o null si no hay tareas completadas.
     */
    public Double findAverageDistanceOfCompletedTasks(Long userId, String userLocationWkt) {
        String sql = """
            SELECT AVG(ST_Distance(
                CAST(? AS geography),
                location::geography
            )) 
            FROM tasks 
            WHERE user_id = ? 
            AND status = 'COMPLETED'
            AND location IS NOT NULL
            """;

        try {
            return jdbcTemplate.queryForObject(
                    sql,
                    Double.class,
                    userLocationWkt,
                    userId
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * Método para contar las tareas por sector y usuarios.
     * @return lista con el conteo de tareas asignadas a cada usuario,
     * agrupadas por sector.
     */
    public List<Map<String, Object>> countTasksByAllUsersAndSectors() {
        String sql = """
        SELECT 
            u.id AS user_id,
            u.name AS user_name,
            s.name AS sector_name, 
            COUNT(t.id) AS task_count
        FROM 
            tasks t
        JOIN 
            users u ON t.user_id = u.id
        JOIN 
            sectors s ON t.sector_id = s.id
        GROUP BY 
            u.id, u.name, s.name
        ORDER BY 
            u.name, s.name
        """;

        return jdbcTemplate.queryForList(sql);
    }
}