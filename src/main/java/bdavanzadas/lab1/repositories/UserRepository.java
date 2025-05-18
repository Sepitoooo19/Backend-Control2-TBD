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
     * Metodo para encontrar un usuario por su username
     * @param "username" El username del usuario a buscar.
     * @return El usuario encontrado.
     *
     *
     * */
    public UserEntity findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{username}, (rs, rowNum) ->
                    new UserEntity(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("role"),
                            rs.getString("name")

                    ));
        } catch (EmptyResultDataAccessException e) {
            return null; // Retorna null si no encuentra el usuario
        }
    }


    /**
     * Metodo para guardar un usuario en la base de datos.
     *
     * Lo que realiza por dentro es lo siguiente:
     * 1. Prepara la consulta SQL para insertar un nuevo usuario en la tabla "users".
     * 2. Utiliza un KeyHolder para obtener la clave generada automáticamente (ID) después de la inserción.
     * 3. Ejecuta la consulta de inserción utilizando el JdbcTemplate y el KeyHolder.
     * 4. Si la inserción es exitosa, se obtiene el ID generado y se establece en el objeto UserEntity.
     * @param "user" El usuario a guardar.
     *
     * @return void
     *
     *
     * */
    public void save(UserEntity user) {
        String sql = "INSERT INTO users (username, password, role, name) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            // La clave importante aquí es "Statement.RETURN_GENERATED_KEYS"
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole());
            ps.setString(4, user.getName());
            return ps;
        }, keyHolder);

        // Aquí obtenemos el ID generado con mayor precisión
        Map<String, Object> keys = keyHolder.getKeys();
        if (keys != null && keys.containsKey("id")) {
            user.setId(((Number) keys.get("id")).intValue());
        } else {
            throw new IllegalStateException("No se pudo generar el ID para el usuario");
        }
    }
}