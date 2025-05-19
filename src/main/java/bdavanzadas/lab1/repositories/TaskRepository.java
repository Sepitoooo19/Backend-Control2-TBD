package bdavanzadas.lab1.repositories;

import bdavanzadas.lab1.entities.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
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
            """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setObject(3, task.getDueDate());
            ps.setString(4, task.getStatus());
            ps.setInt(5, task.getUserId());
            ps.setInt(6, task.getSectorId());
            ps.setString(7, task.getLocation()); // WKT
            return ps;
        }, keyHolder);

        // Obtener ID generado
        Map<String, Object> keys = keyHolder.getKeys();
        if (keys != null && keys.containsKey("id")) {
            task.setId(((Number) keys.get("id")).intValue());
        }

        return task;
    }

    // Read (by ID)
    public TaskEntity findById(int id) {
        String sql = """
            SELECT 
                id, title, description, due_date, status, 
                user_id, sector_id, ST_AsText(location) as location, created_at
            FROM tasks 
            WHERE id = ?
            """;

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                new TaskEntity(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getObject("due_date", LocalDateTime.class),
                        rs.getString("status"),
                        rs.getInt("user_id"),
                        rs.getInt("sector_id"),
                        rs.getString("location"), // WKT
                        rs.getObject("created_at", LocalDateTime.class)
                ), id);
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
        String sql = """
            UPDATE tasks SET 
                title = ?, 
                description = ?, 
                due_date = ?, 
                status = ?, 
                user_id = ?, 
                sector_id = ?, 
                location = ST_GeomFromText(?, 4326)
            WHERE id = ?
            """;

        int updated = jdbcTemplate.update(sql,
                task.getTitle(),
                task.getDescription(),
                task.getDueDate(),
                task.getStatus(),
                task.getUserId(),
                task.getSectorId(),
                task.getLocation(), // WKT
                task.getId()
        );

        return updated > 0;
    }

    // Delete
    public boolean deleteById(int id) {
        String sql = "DELETE FROM tasks WHERE id = ?";
        int deleted = jdbcTemplate.update(sql, id);
        return deleted > 0;
    }
}
