package ipss.cl.sgpp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Configuración de JPA Auditing para tracking automático de cambios.
 * Habilita el uso de @CreatedDate y @LastModifiedDate en las entidades JPA.
 * 
 * Nota: Los campos @CreatedBy y @LastModifiedBy fueron removidos ya que sin sistema
 * de autenticación siempre retornaban "system" sin proporcionar valor real.
 * Solo se mantiene auditoría de fechas (createdAt, updatedAt).
 * 
 * @author SGPP Team
 * @since 1.0
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
    // No requiere configuración adicional - JPA Auditing maneja automáticamente
    // las anotaciones @CreatedDate y @LastModifiedDate
}
