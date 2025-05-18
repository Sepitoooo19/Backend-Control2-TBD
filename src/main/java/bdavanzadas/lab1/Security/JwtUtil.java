package bdavanzadas.lab1.Security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * Clase utilitaria para manejo de tokens JWT
 */
@Component
public class JwtUtil {
    /**
     * Clave secreta para firmar los tokens
     */
    private static final String SECRET = "clave_super_secreta_clave_super_secreta";

    /**
     * Tiempo de expiración del token (10 horas)
     */
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 10;

    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    /**
     * Método para generar un token JWT
     * @param "username" El nombre de usuario
     * @param "role" El rol del usuario
     * @param "userId" El ID del usuario
     * @return El token generado
     */
    public String generateToken(String username, String role, Long userId) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Método para extraer el rol de un token
     * @param "token" El token JWT
     * @return El rol contenido en el token
     */
    public String extractRole(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    /**
     * Método para extraer el ID de usuario de un token
     * @param "token" El token JWT
     * @return El ID de usuario contenido en el token
     */
    public Long extractUserId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("userId", Long.class);
    }

    /**
     * Método para validar un token JWT
     * @param "token" El token a validar
     * @return true si el token es válido, false si no lo es
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}