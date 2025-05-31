package bdavanzadas.lab1.repositories;

import bdavanzadas.lab1.entities.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public class TaskRepository implements TaskRepositoryInt{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Create
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

    // Read (by ID)
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
                task.setLocation(rs.getString("location")); // Ya viene como WKT gracias a ST_AsText()
                task.setCreatedAt(rs.getTimestamp("created_at") != null ?
                        rs.getTimestamp("created_at").toLocalDateTime() : null);
                return task;
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    // Read (All)
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

    // Update
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
                    task.getLocation(), // Esto debe ser el string WKT: "POINT(12.48 41.896)"
                    task.getId()
            );

            System.out.println("Filas afectadas: " + rowsAffected); // Debug
            return rowsAffected > 0;
        } catch (Exception e) {
            System.err.println("Error actualizando tarea: " + e.getMessage());
            e.printStackTrace(); // Para ver el stack trace completo
            return false;
        }
    }

    // Delete
    public boolean deleteById(int id) {
        String sql = "DELETE FROM tasks WHERE id = ?";
        int deleted = jdbcTemplate.update(sql, id);
        return deleted > 0;
    }
}
