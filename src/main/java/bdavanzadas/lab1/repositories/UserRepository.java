package bdavanzadas.lab1.repositories;

import bdavanzadas.lab1.entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;



/**
 *
 * La clase UserRepository representa el repositorio de usuarios en la base de datos.
 * Esta clase contiene métodos para guardar y buscar usuarios en la base de datos.
 * */

@Repository
public class UserRepository implements UserRepositoryInt {


    /**
     * JdbcTemplate es una clase de Spring que simplifica el acceso a la base de datos.
     * Se utiliza para ejecutar consultas SQL y mapear los resultados a objetos Java.
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
        * Método para guardar un nuevo usuario en la base de datos.
     * @param "user" El objeto UserEntity que representa al usuario a guardar.
     * Este método utiliza una consulta SQL para insertar un nuevo usuario en la tabla de usuarios.
     * La contraseña se codifica antes de guardarla en la base de datos.
     * Si el nombre de usuario ya está en uso, se lanza una excepción.

     * */
    // Guardar usuario con WKT
    public void save(UserEntity user) {
        String sql = "INSERT INTO users (username, password, role, name, location) " +
                "VALUES (?, ?, ?, ?, ST_GeomFromText(?, 4326))";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole());
            ps.setString(4, user.getName());
            ps.setString(5, user.getLocation());
            return ps;
        }, keyHolder);

        // Solución: Obtener el ID específicamente del Map
        Map<String, Object> keys = keyHolder.getKeys();
        if (keys != null && keys.containsKey("id")) {
            user.setId(((Number) keys.get("id")).intValue());
        }
    }

    // En UserRepository
    public UserEntity findById(int id) {
        String sql = """
        SELECT id, username, password, role, name, ST_AsText(location) AS location 
        FROM users 
        WHERE id = ?
        """;

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                        new UserEntity(
                                rs.getInt("id"),
                                rs.getString("username"),
                                rs.getString("password"),
                                rs.getString("role"),
                                rs.getString("name"),
                                rs.getString("location")
                        ),
                id
        );
    }


    /**
     * Metodo para encontrar un usuario por su username
     * @param "username" El username del usuario a buscar.
     * @return El usuario encontrado.
     *
     *
     * */
    // Buscar usuario por username (devuelve WKT)
    public UserEntity findByUsername(String username) {
        String sql = "SELECT id, username, password, role, name, ST_AsText(location) AS location " +
                "FROM users WHERE username = ?";

        return jdbcTemplate.queryForObject(sql, new Object[]{username}, (rs, rowNum) -> {
            UserEntity user = new UserEntity();
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setRole(rs.getString("role"));
            user.setName(rs.getString("name"));
            user.setLocation(rs.getString("location")); // WKT: "POINT(-70.651 -33.456)"
            return user;
        });
    }

    /**
     * Metodo para encontrar a todos los usuarios
     *
     * */

    // Obtener todos los usuarios
    public List<UserEntity> findAll() {
        String sql = "SELECT id, username, password, role, name, ST_AsText(location) AS location FROM users";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new UserEntity(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("name"),
                        rs.getString("location") // WKT
                )
        );
    }

    // Eliminar usuario por ID
    public boolean deleteById(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        int affectedRows = jdbcTemplate.update(sql, id);
        return affectedRows > 0;
    }

    // Actualizar usuario (excepto contraseña)
    public boolean updateUser(int id, String username, String name, String role, String wktPoint) {
        String sql = "UPDATE users SET username = ?, name = ?, role = ?, location = ST_GeomFromText(?, 4326) WHERE id = ?";
        int affectedRows = jdbcTemplate.update(
                sql,
                username,
                name,
                role,
                wktPoint,
                id
        );
        return affectedRows > 0;
    }

    public boolean updateUserLocation(int id, String wktPoint) {
        String sql = "UPDATE users SET location = ST_GeomFromText(?, 4326) WHERE id = ?";

        try {
            int affectedRows = jdbcTemplate.update(sql, wktPoint, id);
            return affectedRows > 0;
        } catch (Exception e) {
            System.err.println("Error actualizando ubicación para usuario ID " + id + ": " + e.getMessage());
            System.err.println("WKT: " + wktPoint);
            throw new RuntimeException("Error al actualizar ubicación en base de datos", e);
        }
    }

    // Actualizar contraseña (método separado por seguridad)
    public boolean updatePassword(int id, String newEncodedPassword) {
        String sql = "UPDATE users SET password = ? WHERE id = ?";
        return jdbcTemplate.update(sql, newEncodedPassword, id) > 0;
    }

    public String findUserLocationById(int userId) {
        String sql = "SELECT ST_AsText(location) as location_wkt FROM users WHERE id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, String.class, userId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

}