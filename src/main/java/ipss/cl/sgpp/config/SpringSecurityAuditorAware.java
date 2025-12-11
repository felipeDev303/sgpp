package ipss.cl.sgpp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * Implementación de AuditorAware para Spring Security.
 * Obtiene el usuario autenticado actual del contexto de seguridad.
 * 
 * Esta clase se utilizará cuando Spring Security esté habilitado.
 * Por ahora, JpaAuditingConfig usa una implementación simple que retorna "system".
 * 
 * @author SGPP Team
 * @since 1.0
 */
@Configuration
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    /**
     * Obtiene el email del usuario autenticado actual.
     * Si no hay usuario autenticado o la autenticación no está disponible,
     * retorna "system" como valor por defecto.
     * 
     * @return Optional con el email del usuario autenticado o "system"
     */
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
            .map(SecurityContext::getAuthentication)
            .filter(Authentication::isAuthenticated)
            .map(Authentication::getName)
            .or(() -> Optional.of("system"));
    }
}
