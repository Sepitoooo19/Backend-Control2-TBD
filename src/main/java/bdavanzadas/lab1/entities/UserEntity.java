package bdavanzadas.lab1.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;

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

    @Schema(description = "ID del usuario", example = "1")
    private int id;

    @Schema(description = "Nombre de usuario", example = "johndoe")
    private String username;

    @Schema(description = "Contraseña del usuario", example = "password123")
    private String password;

    @Schema(description = "Rol del usuario", example = "ADMIN")
    private String role; // ADMIN, USER

    @Schema(description = "Nombre del usuario", example = "John Doe")
    private String name;

    @Schema(description = "Ubicación del usuario en formato WKT", example = "POINT(-70.651 -33.456)")
    private String location; // Formato WKT: "POINT(-70.651 -33.456)"
}