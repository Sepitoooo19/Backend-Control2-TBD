package bdavanzadas.lab1.services;

import bdavanzadas.lab1.entities.UserEntity;
import bdavanzadas.lab1.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Map;


/**
 *
 * La clase UserService representa el servicio de usuarios en la aplicación.
 * Esta clase contiene métodos para registrar usuarios, validar credenciales y obtener el ID del usuario autenticado.
 *
 *
 * */
@Service
public class UserService {

    /**
     * Repositorio de usuarios.
     * Este repositorio se utiliza para interactuar con la base de datos de usuarios.
     */
    @Autowired
    private UserRepository userRepository;


    /**
     * Codificador de contraseñas.
     * Este codificador se utiliza para codificar las contraseñas de los usuarios antes de guardarlas en la base de datos.
     */
    @Autowired
    private PasswordEncoder encoder;

    /**
     * Registra un nuevo usuario administrador con ubicación geográfica.
     * @param username Nombre de usuario (único)
     * @param password Contraseña (se hashea automáticamente)
     * @param name Nombre completo
     * @param role Rol del usuario (debe ser "ADMIN")
     * @param wktPoint Ubicación en formato WKT ("POINT(longitud latitud)")
     * @throws IllegalArgumentException Si el WKT es inválido
     */
    public void registerAdmin(String username, String password, String name, String role, String wktPoint) {
        if (wktPoint == null || !wktPoint.startsWith("POINT(")) {
            throw new IllegalArgumentException("Formato WKT inválido. Ejemplo: 'POINT(-70.651 -33.456)'");
        }

        String encodedPassword = encoder.encode(password);
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.setRole("ADMIN"); // Fuerza el rol ADMIN
        user.setName(name);
        user.setLocation(wktPoint);
        userRepository.save(user);
    }

    /**
     * Registra un nuevo usuario estándar con ubicación geográfica.
     * @param username Nombre de usuario (único)
     * @param password Contraseña (se hashea automáticamente)
     * @param name Nombre completo
     * @param role Rol del usuario (debe ser "USER")
     * @param wktPoint Ubicación en formato WKT ("POINT(longitud latitud)")
     * @throws IllegalArgumentException Si el WKT es inválido
     */
    public void registerUser(String username, String password, String name, String role, String wktPoint) {
        if (wktPoint == null || !wktPoint.startsWith("POINT(")) {
            throw new IllegalArgumentException("Formato WKT inválido. Ejemplo: 'POINT(-70.651 -33.456)'");
        }

        String encodedPassword = encoder.encode(password);
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.setRole("USER"); // Fuerza el rol USER
        user.setName(name);
        user.setLocation(wktPoint);
        userRepository.save(user);
    }
    /**
     * Metodo para validar las credenciales de un usuario.
     * @param "username" El nombre de usuario a validar.
     * @param "password" La contraseña a validar.
     * @return El usuario encontrado si las credenciales son válidas, null en caso contrario.
     *
     * Este metodo busca un usuario por su nombre de usuario y valida la contraseña.
     */

    public UserEntity validateCredentials(String username, String password) {
        UserEntity user = userRepository.findByUsername(username);
        if (user != null && encoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }


    /**
     * Método para obtener el ID del usuario autenticado como int.
     * @return El ID del usuario autenticado (int).
     * @throws RuntimeException Si no hay usuario autenticado o el tipo no es compatible.
     */
    public int getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Integer) {
            return (int) authentication.getPrincipal();
        } else if (authentication != null && authentication.getPrincipal() instanceof Long) {
            // Conversión segura de Long a int (si el ID cabe en un int)
            long userId = (Long) authentication.getPrincipal();
            if (userId > Integer.MAX_VALUE || userId < Integer.MIN_VALUE) {
                throw new RuntimeException("El ID de usuario excede el límite de int");
            }
            return (int) userId;
        }
        throw new RuntimeException("Usuario no autenticado o tipo de ID no soportado");
    }

    // Obtener todos los usuarios
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    // Obtener usuario por ID
    public UserEntity getUserById(int id) {
        return userRepository.findById(id);
    }


    public UserEntity getAuthenticatedUserProfile() {
        int userId = getAuthenticatedUserId(); // Usa tu método existente

        return userRepository.findById(userId);
    }


    // Eliminar usuario
    public boolean deleteUser(int id) {
        return userRepository.deleteById(id);
    }

    // Actualizar datos de usuario
    public boolean updateUser(int id, String username, String name, String role, String wktPoint) {
        if (wktPoint != null && !wktPoint.startsWith("POINT(")) {
            throw new IllegalArgumentException("Formato WKT inválido");
        }
        return userRepository.updateUser(id, username, name, role, wktPoint);
    }

    public boolean updateAuthenticatedUser(Map<String, String> updates) {
        // Obtener ID del usuario autenticado
        int authenticatedUserId = getAuthenticatedUserId();

        // Validar que el usuario solo se actualice a sí mismo
        if (updates.containsKey("id")) {
            throw new SecurityException("No puedes modificar tu ID de usuario");
        }

        // Validar formato WKT si se está actualizando la ubicación
        String wktPoint = updates.get("location");
        if (wktPoint != null && !wktPoint.startsWith("POINT(")) {
            throw new IllegalArgumentException("Formato WKT inválido. Use 'POINT(longitud latitud)'");
        }

        // Actualizar solo los campos proporcionados
        return userRepository.updateUser(
                authenticatedUserId,
                updates.get("username"),
                updates.get("name"),
                updates.get("role"),
                wktPoint
        );
    }

    // Actualizar contraseña
    public boolean updatePassword(int id, String newPassword) {
        String encodedPassword = encoder.encode(newPassword);
        return userRepository.updatePassword(id, encodedPassword);
    }

    /**
     * Método para obtener la ubicación del usuario autenticado.
     * @return La ubicación en formato WKT o null si no se encuentra.
     */
    public String getAuthenticatedUserLocation() {
        int userId = getAuthenticatedUserId();
        String location = userRepository.findUserLocationById(userId);

        if (location == null || location.trim().isEmpty()) {
            throw new RuntimeException("El usuario no tiene ubicación registrada");
        }

        // Validar que es un POINT válido
        if (!isValidWktPoint(location)) {
            throw new RuntimeException("Formato de ubicación inválido en la base de datos");
        }

        return location;
    }

    public boolean isValidWktPoint(String wkt) {
        return wkt != null && wkt.matches("^POINT\\([-+]?\\d+\\.?\\d* [-+]?\\d+\\.?\\d*\\)$");
    }

    public Map<String, Double> parseWktToCoordinates(String wkt) {
        if (!wkt.startsWith("POINT(")) {
            throw new IllegalArgumentException("Formato WKT no válido");
        }

        String coordStr = wkt.substring(6, wkt.length() - 1); // Elimina "POINT(" y ")"
        String[] coords = coordStr.split(" ");

        return Map.of(
                "longitude", Double.parseDouble(coords[0]),
                "latitude", Double.parseDouble(coords[1])
        );
    }

}