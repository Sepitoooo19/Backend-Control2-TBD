package bdavanzadas.lab1.services;

import bdavanzadas.lab1.entities.UserEntity;
import bdavanzadas.lab1.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Servicio de usuarios que proporciona operaciones para registrar, autenticar,
 * actualizar y eliminar usuarios, así como gestionar su ubicación geográfica.
 */
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
     * Registra un nuevo usuario con rol de administrador.
     *
     * @param username Nombre de usuario único.
     * @param password Contraseña del usuario.
     * @param name     Nombre completo del usuario.
     * @param role     Rol del usuario (no se utiliza, siempre se asigna "ADMIN").
     * @param wktPoint Ubicación del usuario en formato WKT ("POINT(long lat)").
     * @throws IllegalArgumentException Si el formato del punto es inválido.
     */
    public void registerAdmin(String username, String password, String name, String role, String wktPoint) {
        if (wktPoint == null || !wktPoint.startsWith("POINT(")) {
            throw new IllegalArgumentException("Formato WKT inválido. Ejemplo: 'POINT(-70.651 -33.456)'");
        }

        String encodedPassword = encoder.encode(password);
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.setRole("ADMIN");
        user.setName(name);
        user.setLocation(wktPoint);
        userRepository.save(user);
    }

    /**
     * Registra un nuevo usuario estándar.
     *
     * @param username Nombre de usuario único.
     * @param password Contraseña del usuario.
     * @param name     Nombre completo del usuario.
     * @param role     Rol del usuario (no se utiliza, siempre se asigna "USER").
     * @param wktPoint Ubicación del usuario en formato WKT ("POINT(long lat)").
     * @throws IllegalArgumentException Si el formato del punto es inválido.
     */
    public void registerUser(String username, String password, String name, String role, String wktPoint) {
        if (wktPoint == null || !wktPoint.startsWith("POINT(")) {
            throw new IllegalArgumentException("Formato WKT inválido. Ejemplo: 'POINT(-70.651 -33.456)'");
        }

        String encodedPassword = encoder.encode(password);
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.setRole("USER");
        user.setName(name);
        user.setLocation(wktPoint);
        userRepository.save(user);
    }

    /**
     * Valida las credenciales de un usuario.
     *
     * @param username Nombre de usuario.
     * @param password Contraseña del usuario.
     * @return La entidad de usuario si las credenciales son válidas, null en caso contrario.
     */
    public UserEntity validateCredentials(String username, String password) {
        UserEntity user = userRepository.findByUsername(username);
        if (user != null && encoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    /**
     * Obtiene el ID del usuario autenticado desde el contexto de seguridad.
     *
     * @return ID del usuario autenticado.
     * @throws RuntimeException Si no hay usuario autenticado o el tipo de ID no es compatible.
     */
    public int getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Integer) {
            return (int) authentication.getPrincipal();
        } else if (authentication != null && authentication.getPrincipal() instanceof Long) {
            long userId = (Long) authentication.getPrincipal();
            if (userId > Integer.MAX_VALUE || userId < Integer.MIN_VALUE) {
                throw new RuntimeException("El ID de usuario excede el límite de int");
            }
            return (int) userId;
        }
        throw new RuntimeException("Usuario no autenticado o tipo de ID no soportado");
    }

    /**
     * Retorna una lista con todos los usuarios registrados.
     *
     * @return Lista de entidades de usuario.
     */
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Obtiene un usuario por su ID.
     *
     * @param id ID del usuario.
     * @return Entidad del usuario correspondiente o null si no existe.
     */
    public UserEntity getUserById(int id) {
        return userRepository.findById(id);
    }

    /**
     * Obtiene el perfil del usuario autenticado.
     *
     * @return Entidad del usuario autenticado.
     */
    public UserEntity getAuthenticatedUserProfile() {
        int userId = getAuthenticatedUserId();
        return userRepository.findById(userId);
    }

    /**
     * Elimina un usuario por su ID.
     *
     * @param id ID del usuario a eliminar.
     * @return true si se eliminó correctamente, false si no se encontró.
     */
    public boolean deleteUser(int id) {
        return userRepository.deleteById(id);
    }

    /**
     * Actualiza los datos de un usuario.
     *
     * @param id        ID del usuario.
     * @param username  Nuevo nombre de usuario.
     * @param name      Nuevo nombre completo.
     * @param role      Nuevo rol.
     * @param wktPoint  Nueva ubicación en formato WKT.
     * @return true si se actualizó correctamente, false si falló.
     * @throws IllegalArgumentException Si el formato del punto es inválido.
     */
    public boolean updateUser(int id, String username, String name, String role, String wktPoint) {
        if (wktPoint != null && !wktPoint.startsWith("POINT(")) {
            throw new IllegalArgumentException("Formato WKT inválido");
        }
        return userRepository.updateUser(id, username, name, role, wktPoint);
    }

    /**
     * Actualiza la ubicación geográfica del usuario autenticado.
     *
     * @param latitude  Latitud (-90 a 90).
     * @param longitude Longitud (-180 a 180).
     * @return true si se actualizó correctamente, false en caso de error.
     * @throws IllegalArgumentException Si las coordenadas son inválidas.
     */
    public boolean updateAuthenticatedUserLocation(double latitude, double longitude) {
        int authenticatedUserId = getAuthenticatedUserId();

        if (latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException("Latitud debe estar entre -90 y 90 grados");
        }
        if (longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("Longitud debe estar entre -180 y 180 grados");
        }

        String wktPoint = String.format("POINT(%f %f)", longitude, latitude);

        try {
            return userRepository.updateUserLocation(authenticatedUserId, wktPoint);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar ubicación del usuario autenticado", e);
        }
    }

    /**
     * Actualiza la contraseña de un usuario.
     *
     * @param id          ID del usuario.
     * @param newPassword Nueva contraseña (sin encriptar).
     * @return true si se actualizó correctamente, false si falló.
     */
    public boolean updatePassword(int id, String newPassword) {
        String encodedPassword = encoder.encode(newPassword);
        return userRepository.updatePassword(id, encodedPassword);
    }

    /**
     * Obtiene la ubicación del usuario autenticado.
     *
     * @return Ubicación en formato WKT.
     * @throws RuntimeException Si no se encuentra la ubicación o es inválida.
     */
    public String getAuthenticatedUserLocation() {
        int userId = getAuthenticatedUserId();
        String location = userRepository.findUserLocationById(userId);

        if (location == null || location.trim().isEmpty()) {
            throw new RuntimeException("El usuario no tiene ubicación registrada");
        }

        if (!isValidWktPoint(location)) {
            throw new RuntimeException("Formato de ubicación inválido en la base de datos");
        }

        return location;
    }

    /**
     * Valida que un string tenga formato de punto WKT válido.
     *
     * @param wkt Cadena en formato WKT.
     * @return true si es un "POINT(lon lat)" válido, false si no lo es.
     */
    public boolean isValidWktPoint(String wkt) {
        return wkt != null && wkt.matches("^POINT\\([-+]?\\d+\\.?\\d* [-+]?\\d+\\.?\\d*\\)$");
    }

    /**
     * Parsea un string WKT a un mapa con las coordenadas numéricas.
     *
     * @param wkt Cadena WKT en formato "POINT(long lat)".
     * @return Mapa con las claves "longitude" y "latitude".
     * @throws IllegalArgumentException Si el formato WKT es inválido.
     */
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
