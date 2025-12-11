package ipss.cl.sgpp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Configuración de JPA Auditing para tracking automático de cambios.
 * Habilita el uso de @CreatedDate, @LastModifiedDate, @CreatedBy y @LastModifiedBy
 * en las entidades JPA.
 * 
 * Utiliza SpringSecurityAuditorAware para obtener el usuario autenticado actual.
 * Si Spring Security no está habilitado, retorna "system" como valor por defecto.
 * 
 * @author SGPP Team
 * @since 1.0
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfig {

    private final SpringSecurityAuditorAware springSecurityAuditorAware;

    /**
     * Constructor con inyección de dependencias.
     * 
     * @param springSecurityAuditorAware implementación de AuditorAware con Spring Security
     */
    public JpaAuditingConfig(SpringSecurityAuditorAware springSecurityAuditorAware) {
        this.springSecurityAuditorAware = springSecurityAuditorAware;
    }

    /**
     * Proveedor del auditor actual (usuario que realiza la operación).
     * Utiliza SpringSecurityAuditorAware para obtener el usuario autenticado.
     * 
     * Comportamiento:
     * - Con Spring Security habilitado: retorna el email del usuario autenticado
     * - Sin Spring Security: retorna "system" como valor por defecto
     * 
     * @return AuditorAware que provee el nombre del usuario actual
     */
    @Bean
    public AuditorAware<String> auditorProvider() {
        return springSecurityAuditorAware;
    }
}
