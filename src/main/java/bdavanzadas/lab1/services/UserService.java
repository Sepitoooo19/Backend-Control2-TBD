package bdavanzadas.lab1.services;

import bdavanzadas.lab1.entities.UserEntity;
import bdavanzadas.lab1.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


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
     * Método para registrar un nuevo usuario administrador.
     * @param username El nombre de usuario del nuevo administrador.
     * @param password La contraseña del nuevo administrador.
     * @param name El nombre completo del administrador.
     *
     * Este método codifica la contraseña del nuevo administrador y la guarda en la base de datos.
     * Si el nombre de usuario ya está en uso, se lanza una excepción.
     */
    public void registerAdmin(String username, String password, String name) {
        String encodedPassword = encoder.encode(password);
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.setRole("ADMIN");
        user.setName(name);
        userRepository.save(user);
    }

    /**
     * Método para registrar un nuevo usuario.
     * @param username El nombre de usuario del usuario.
     * @param password La contraseña del nuevo usuario.
     * @param name El nombre completo del usuario.
     *
     * Este método codifica la contraseña del nuevo usuario y la guarda en la base de datos.
     * Si el nombre de usuario ya está en uso, se lanza una excepción.
     */
    public void registerUser(String username, String password, String name) {
        String encodedPassword = encoder.encode(password);
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.setRole("USER");
        user.setName(name);
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
}