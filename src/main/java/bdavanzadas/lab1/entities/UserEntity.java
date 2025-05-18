package bdavanzadas.lab1.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

/**
 *
 *  La clase UserEntity representa la entidad de usuario en la base de datos.
 *  Esta clase contiene información básica sobre el usuario, como su nombre de usuario, contraseña y rol.
 *  Los roles pueden ser ADMIN, CLIENT o DEALER.
 *
 */
public class UserEntity {
    private int id;
    private String username;
    private String password;
    private String role; // ADMIN, USER
    private String name;

}