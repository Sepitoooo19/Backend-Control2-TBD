package bdavanzadas.lab1.services;

import bdavanzadas.lab1.entities.UserEntity;
import bdavanzadas.lab1.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;


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
     * Metodo para obtener el ID del usuario autenticado.
     * @return El ID del usuario autenticado.
     *
     * Este metodo obtiene el ID del usuario autenticado a partir del contexto de seguridad.
     * Si no hay un usuario autenticado, lanza una excepción.
     */
    public Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Long) {
            return (Long) authentication.getPrincipal(); // Retorna el ID del usuario autenticado
        }
        throw new RuntimeException("Usuario no autenticado");
    }

    // Obtener todos los usuarios
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
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

    // Actualizar contraseña
    public boolean updatePassword(int id, String newPassword) {
        String encodedPassword = encoder.encode(newPassword);
        return userRepository.updatePassword(id, encodedPassword);
    }
}