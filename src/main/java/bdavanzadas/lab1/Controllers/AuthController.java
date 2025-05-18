package bdavanzadas.lab1.Controllers;


import bdavanzadas.lab1.services.UserService;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import bdavanzadas.lab1.entities.UserEntity;
import bdavanzadas.lab1.Security.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;


import org.springframework.http.HttpStatus;

import java.util.Map;


/**
 *
 * La clase AuthController maneja las solicitudes de autenticación y registro de usuarios.
 * Esta clase contiene métodos para registrar nuevos usuarios y validar credenciales de inicio de sesión.
 *
 *
 * */
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {


    /**
     * Servicio de usuarios.
     * Este servicio se utiliza para interactuar con la base de datos de usuarios.
     */
    @Autowired
    private UserService userService;



    /**
     *
     * JwtUtil es una clase de utilidad para manejar la generación y validación de tokens JWT.
     * Esta clase se utiliza para generar tokens JWT para los usuarios autenticados y validar tokens en solicitudes posteriores.
     *
     *
     * */
    @Autowired
    private JwtUtil jwtUtil;







    /**
     * Endpoint para registrar un nuevo usuario.
     * Este endpoint recibe un objeto JSON con los datos del nuevo usuario y lo registra en la base de datos.
     *
     * @param body El objeto JSON con los datos del nuevo usuario (username, password, role, name y location)
     * @return Una respuesta HTTP con el resultado del registro.
     */



    @Operation(summary = "Registrar un nuevo usuario",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del usuario a registrar",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Ejemplo básico",
                                    summary = "Registro con ubicación WKT",
                                    value = """
                    {
                        "username": "usuario_ejemplo",
                        "password": "claveSegura123",
                        "role": "USER",
                        "name": "Juan Pérez",
                        "location": "POINT(-70.651 -33.456)"
                    }""",
                                    description = "El campo 'location' debe ser un WKT válido (POINT)"
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario registrado"),
                    @ApiResponse(responseCode = "400", description = "Error de validación")
            }
    )
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, Object> body) {
        try {
            String username = (String) body.get("username");
            String password = (String) body.get("password");
            String role = (String) body.get("role");
            String name = (String) body.get("name");
            String locationWKT = (String) body.get("location");

            // Validaciones básicas
            if (username == null || username.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "message", "El nombre de usuario es requerido"));
            }

            if (password == null || password.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "message", "La contraseña es requerida"));
            }

            if (role == null || role.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "message", "El rol es requerido"));
            }

            if (name == null || name.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "message", "El nombre completo es requerido"));
            }

            if (locationWKT == null || !locationWKT.startsWith("POINT(")) {
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "message", "Ubicación inválida. Formato: 'POINT(longitud latitud)'"));
            }

            // Lógica de registro (actualizada para coincidir con los services)
            if ("ADMIN".equalsIgnoreCase(role)) {
                userService.registerAdmin(username, password, name, role, locationWKT);
            } else if ("USER".equalsIgnoreCase(role)) {
                userService.registerUser(username, password, name, role, locationWKT);
            } else {
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "message", "Rol no válido. Los roles permitidos son ADMIN o USER"));
            }

            return ResponseEntity.ok(Map.of("success", true, "message", "Usuario registrado exitosamente"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Error al registrar usuario: " + e.getMessage()));
        }
    }
    /**
     * Endpoint para loggearse en la aplicación.
     * Este endpoint recibe un objeto JSON con el nombre de usuario y la contraseña del usuario.
     * Si las credenciales son válidas, se genera un token JWT y se devuelve en la respuesta.
     * @param "body" El objeto JSON con el nombre de usuario y la contraseña del usuario.
     * @return Una respuesta HTTP con el token JWT y el rol del usuario.
     *
     *
     * */


    @Operation(summary = "Iniciar sesión",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Credenciales del usuario",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Ejemplo básico",
                                    summary = "Inicio de sesión con credenciales válidas",
                                    value = """
                    {
                        "username": "usuario_ejemplo",
                        "password": "claveSegura123"
                    }""",
                                    description = "El campo 'username' y 'password' son requeridos"
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso"),
                    @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
            }

            )

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        // Validar las credenciales del usuario
        UserEntity user = userService.validateCredentials(username, password);
        if (user != null) {
            // Generar el token JWT con el username, role y userId
            String token = jwtUtil.generateToken(user.getUsername(), user.getRole(), (long) user.getId());

            // Devolver el token y el rol en la respuesta
            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "role", user.getRole() // Incluye el rol del usuario
            ));
        } else {
            // Respuesta en caso de credenciales inválidas
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "success", false,
                    "message", "Credenciales inválidas"
            ));
        }
    }



    /**
     * Endpoint para actualizar la contraseña de un usuario.
     * Este endpoint recibe un objeto JSON con la nueva contraseña del usuario.
     * @param "id" El ID del usuario cuya contraseña se va a actualizar.
     * @param "request" El objeto JSON con la nueva contraseña del usuario.
     * @return Una respuesta HTTP con el resultado de la actualización.
     *
     *
     * */

    @Operation(summary = "Actualizar contraseña de usuario",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Nueva contraseña del usuario",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Ejemplo básico",
                                    summary = "Actualización de contraseña",
                                    value = """
                    {
                        "newPassword": "nuevaContraseña123"
                    }""",
                                    description = "El campo 'newPassword' es requerido"
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Contraseña actualizada"),
                    @ApiResponse(responseCode = "400", description = "Error de validación")
            }

            )
    @PatchMapping("/user/{id}/password")
    public ResponseEntity<?> updatePassword(
            @PathVariable int id,
            @RequestBody Map<String, String> request
    ) {
        String newPassword = request.get("newPassword");
        if (newPassword == null || newPassword.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "La contraseña es requerida"));
        }
        boolean updated = userService.updatePassword(id, newPassword);
        return ResponseEntity.ok(Map.of("success", updated));
    }
}