package ipss.cl.sgpp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración de OpenAPI 3.0 para documentación de la API REST.
 * Proporciona metadata, información de contacto, servidores y configuración de Swagger UI.
 * 
 * <p>Acceso a la documentación:</p>
 * <ul>
 *   <li>Swagger UI: <a href="http://localhost:8080/swagger-ui.html">http://localhost:8080/swagger-ui.html</a></li>
 *   <li>OpenAPI JSON: <a href="http://localhost:8080/v3/api-docs">http://localhost:8080/v3/api-docs</a></li>
 * </ul>
 * 
 * @author SGPP Team
 * @since 1.0
 */
@Configuration
public class OpenApiConfig {

    /**
     * Configura la información de OpenAPI para la documentación de la API.
     * 
     * @return Configuración de OpenAPI con metadata de la API
     */
    @Bean
    public OpenAPI sgppOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SGPP - Sistema de Gestión de Prácticas Profesionales")
                        .description("""
                                API REST para la gestión integral de prácticas profesionales.
                                
                                **Funcionalidades principales:**
                                - Gestión de prácticas (CRUD completo)
                                - Consulta de estudiantes, profesores, empresas y supervisores
                                - Control de estados de práctica (PENDIENTE, EN_CURSO, COMPLETADA)
                                - Separación de responsabilidades por rol (estudiante/profesor)
                                - Auditoría automática de cambios temporales
                                - Borrado lógico de prácticas
                                
                                **Tecnologías:**
                                - Spring Boot 3.5.8
                                - Java 21
                                - PostgreSQL
                                - Flyway (migraciones)
                                
                                **Nota:** Esta API no implementa autenticación. El testing se realiza manualmente.
                                """)
                        .version("0.0.1-SNAPSHOT")
                        .contact(new Contact()
                                .name("SGPP Team")
                                .email("sgpp@ipss.cl")
                                .url("https://github.com/felipeDev303/sgpp"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor de desarrollo local")
                ));
    }
}
