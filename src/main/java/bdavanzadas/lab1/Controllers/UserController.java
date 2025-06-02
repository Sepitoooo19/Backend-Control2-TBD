package bdavanzadas.lab1.Controllers;

import bdavanzadas.lab1.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import bdavanzadas.lab1.entities.UserEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;




    @Operation(summary = "Obtener todos los usuarios",
            description = "Retorna una lista de todos los usuarios registrados con sus ubicaciones en formato WKT",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserEntity.class)))
                    ),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")}
    )


    @GetMapping
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Eliminar usuario",
            description = "Elimina un usuario por su ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario eliminado exitosamente",
                            content = @Content(schema = @Schema(example = "{\"success\": true, \"message\": \"Usuario eliminado\"}"))),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
            })

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        if (userService.deleteUser(id)) {
            return ResponseEntity.ok(Map.of("success", true, "message", "Usuario eliminado"));
        }
        return ResponseEntity.status(404).body(Map.of("success", false, "message", "Usuario no encontrado"));
    }



    @Operation(summary = "Actualizar usuario",
            description = "Actualiza los datos de un usuario existente",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Campos a actualizar (username, name, role, location)",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                               {
                                   "username": "nuevo_username",
                                   "name": "Nuevo Nombre",
                                   "role": "USER",
                                   "location": "POINT(-70.700 -33.500)"
                               }"""
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario actualizado",
                            content = @Content(schema = @Schema(example = "{\"success\": true}"))),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos"),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
            })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable int id,
            @RequestBody Map<String, String> updates
    ) {
        try {
            boolean updated = userService.updateUser(
                    id,
                    updates.get("username"),
                    updates.get("name"),
                    updates.get("role"),
                    updates.get("location")
            );
            return ResponseEntity.ok(Map.of("success", updated));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateAuthenticatedUser(@RequestBody Map<String, String> updates) {
        try {
            boolean updated = userService.updateAuthenticatedUser(updates);

            return ResponseEntity.ok(Map.of(
                    "success", updated,
                    "message", updated ? "Usuario actualizado" : "No se realizaron cambios"
            ));

        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("success", false, "message", e.getMessage()));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("success", false, "message", "Error al actualizar usuario"));
        }
    }

    @Operation(
            summary = "Obtener perfil del usuario autenticado",
            description = "Devuelve los datos del usuario actualmente logueado",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Perfil obtenido exitosamente",
                            content = @Content(schema = @Schema(implementation = UserEntity.class))
                    ),
                    @ApiResponse(responseCode = "401", description = "No autenticado")
            }
    )
    @GetMapping("/user")
    public ResponseEntity<?> getAuthenticatedUserProfile() {
        try {
            UserEntity user = userService.getAuthenticatedUserProfile();

            // Opcional: Filtrar datos sensibles antes de retornar
            user.setPassword(null);

            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "success", false,
                            "message", e.getMessage()
                    ));
        }
    }


    @Operation(
            summary = "Obtener usuario por ID",
            description = "Endpoint para administradores. Requiere autenticación y rol ADMIN.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Usuario encontrado",
                            content = @Content(schema = @Schema(implementation = UserEntity.class))
                    ),
                    @ApiResponse(responseCode = "401", description = "No autorizado"),
                    @ApiResponse(responseCode = "403", description = "Prohibido (requiere rol ADMIN)"),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
            }
    )
    // Endpoint SOLO para ADMINs
    @PreAuthorize("hasRole('ADMIN')") // <<-- Ahora funciona correctamente
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(
            @Parameter(description = "ID del usuario a buscar", example = "1")
            @PathVariable int id
    ) {
        try {
            UserEntity user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "success", false,
                            "message", e.getMessage()
                    ));
        }
    }

    // getAuthenticatedUserLocation
    @GetMapping("/location")
    public ResponseEntity<?> getAuthenticatedUserLocation() {
        try {
            String wktLocation = userService.getAuthenticatedUserLocation();

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "location", wktLocation,
                    "coordinates", userService.parseWktToCoordinates(wktLocation)
            ));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "success", false,
                            "message", e.getMessage()
                    ));
        }
    }




}