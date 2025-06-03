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
 * La clase UserRepository representa el repositorio de usuarios en la base de datos.
 * Esta clase contiene métodos para realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * sobre la tabla de usuarios en la base de datos, incluyendo manejo de ubicaciones geográficas.
 * Utiliza JdbcTemplate de Spring para la interacción con la base de datos.
 */
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
     * La contraseña debe venir codificada antes de llamar a este método.
     * La ubicación debe estar en formato WKT (Well-Known Text).
     * Si el nombre de usuario ya está en uso, se lanzará una excepción.
     * El ID generado se asigna automáticamente al objeto UserEntity proporcionado.
     */
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

        Map<String, Object> keys = keyHolder.getKeys();
        if (keys != null && keys.containsKey("id")) {
            user.setId(((Number) keys.get("id")).intValue());
        }
    }

    /**
     * Método para encontrar un usuario por su ID.
     * @param "id" El ID del usuario a buscar.
     * @return El objeto UserEntity correspondiente al ID proporcionado.
     * @throws "EmptyResultDataAccessException" si no se encuentra ningún usuario con el ID especificado.
     * La ubicación se devuelve en formato WKT (Well-Known Text).
     */
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
     * Método para encontrar un usuario por su nombre de usuario.
     * @param "username" El nombre de usuario a buscar.
     * @return El objeto UserEntity correspondiente al nombre de usuario proporcionado.
     * @throws "EmptyResultDataAccessException" si no se encuentra ningún usuario con el nombre de usuario especificado.
     * La ubicación se devuelve en formato WKT (Well-Known Text).
     */
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
            user.setLocation(rs.getString("location"));
            return user;
        });
    }

    /**
     * Método para obtener todos los usuarios registrados en el sistema.
     * @return Una lista de objetos UserEntity con todos los usuarios.
     * La ubicación de cada usuario se devuelve en formato WKT (Well-Known Text).
     */
    public List<UserEntity> findAll() {
        String sql = "SELECT id, username, password, role, name, ST_AsText(location) AS location FROM users";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new UserEntity(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("name"),
                        rs.getString("location")
                )
        );
    }

    /**
     * Método para eliminar un usuario por su ID.
     * @param "id" El ID del usuario a eliminar.
     * @return true si se eliminó correctamente, false si no se encontró el usuario.
     */
    public boolean deleteById(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        int affectedRows = jdbcTemplate.update(sql, id);
        return affectedRows > 0;
    }

    /**
     * Método para actualizar la información de un usuario (excepto la contraseña).
     * @param "id" El ID del usuario a actualizar.
     * @param "username" El nuevo nombre de usuario.
     * @param "name" El nuevo nombre real del usuario.
     * @param "role" El nuevo rol del usuario.
     * @param "wktPoint" La nueva ubicación en formato WKT (Well-Known Text).
     * @return true si la actualización fue exitosa, false si no se encontró el usuario.
     */
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

    /**
     * Método para actualizar únicamente la ubicación de un usuario.
     * @param "id" El ID del usuario cuya ubicación se va a actualizar.
     * @param "wktPoint" La nueva ubicación en formato WKT (Well-Known Text).
     * @return true si la actualización fue exitosa, false si no se encontró el usuario.
     * @throws "RuntimeException" si ocurre algún error al actualizar la ubicación.
     */
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

    /**
     * Método para actualizar la contraseña de un usuario.
     * @param "id" El ID del usuario cuya contraseña se va a actualizar.
     * @param "newEncodedPassword" La nueva contraseña ya codificada.
     * @return true si la actualización fue exitosa, false si no se encontró el usuario.
     */
    public boolean updatePassword(int id, String newEncodedPassword) {
        String sql = "UPDATE users SET password = ? WHERE id = ?";
        return jdbcTemplate.update(sql, newEncodedPassword, id) > 0;
    }

    /**
     * Método para obtener la ubicación de un usuario en formato WKT.
     * @param "userId" El ID del usuario cuya ubicación se quiere obtener.
     * @return La ubicación en formato WKT (Well-Known Text), o null si no se encuentra el usuario.
     */
    public String findUserLocationById(int userId) {
        String sql = "SELECT ST_AsText(location) as location_wkt FROM users WHERE id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, String.class, userId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}