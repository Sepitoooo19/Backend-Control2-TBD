package bdavanzadas.lab1.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 *
 * Clase de configuración de CORS para la aplicación.
 * Esta clase permite la configuración de CORS (Cross-Origin Resource Sharing) para la aplicación.
 * */
@Configuration
public class WebConfig {
    //Este código configura la política de CORS (Cross-Origin Resource Sharing) para la aplicación

    /**
     *
     * Configura CORS para permitir solicitudes de diferentes orígenes
     * Esta configuración permite solicitudes de cualquier origen, lo que puede ser útil durante el desarrollo.
     * */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*") // Permitir todos los orígenes (no recomendado en producción)
                        .allowedMethods("*")
                        .allowedHeaders("*");
            }
        };
    }
}
