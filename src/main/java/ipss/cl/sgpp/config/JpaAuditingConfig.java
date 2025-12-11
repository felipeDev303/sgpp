package ipss.cl.sgpp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

/**
 * Configuración de JPA Auditing para tracking automático de cambios.
 * Habilita el uso de @CreatedDate, @LastModifiedDate, @CreatedBy y @LastModifiedBy
 * en las entidades JPA.
 * 
 * @author SGPP Team
 * @since 1.0
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfig {

    /**
     * Proveedor del auditor actual (usuario que realiza la operación).
     * Por ahora retorna "system" como placeholder.
     * En el futuro (con Spring Security), retornará el email del usuario autenticado.
     * 
     * @return AuditorAware que provee el nombre del usuario actual
     */
    @Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }
    
    /**
     * Implementación de AuditorAware para obtener el usuario actual.
     * Retorna "system" como valor por defecto hasta que se implemente Spring Security.
     * 
     * TODO: Integrar con Spring Security para obtener el usuario autenticado:
     * <pre>
     * {@code
     * return Optional.ofNullable(SecurityContextHolder.getContext())
     *     .map(SecurityContext::getAuthentication)
     *     .filter(Authentication::isAuthenticated)
     *     .map(Authentication::getName);
     * }
     * </pre>
     */
    static class AuditorAwareImpl implements AuditorAware<String> {
        
        @Override
        public Optional<String> getCurrentAuditor() {
            // Por ahora retorna "system" como placeholder
            // Cuando se implemente Spring Security, retornará el email del usuario autenticado
            return Optional.of("system");
        }
    }
}
