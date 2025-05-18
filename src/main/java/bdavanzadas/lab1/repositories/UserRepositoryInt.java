package bdavanzadas.lab1.repositories;

import java.util.List;
import bdavanzadas.lab1.entities.UserEntity;


/**
 * La interfaz UserRepositoryInt define los métodos para interactuar con la base de datos de usuarios.
 * Esta interfaz contiene métodos para guardar y buscar usuarios en la base de datos.
 *
 */
public interface UserRepositoryInt {

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
    void save(UserEntity user);

    /**
     * Metodo para encontrar un usuario por su username
     * @param "username" El username del usuario a buscar.
     * @return El usuario encontrado.
     *
     *
     * */
    UserEntity findByUsername(String username);

}
