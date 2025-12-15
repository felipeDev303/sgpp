package ipss.cl.sgpp.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Clase base auditable que proporciona campos de auditoría automáticos.
 * Las entidades que extiendan esta clase tendrán tracking automático de:
 * - Fecha de creación (createdAt)
 * - Fecha de última modificación (updatedAt)
 * 
 * Los valores se establecen automáticamente mediante JPA Auditing.
 * 
 * Nota: Los campos createdBy/updatedBy fueron removidos ya que sin sistema de autenticación
 * siempre retornaban "system" sin proporcionar valor real.
 * 
 * @author SGPP Team
 * @since 1.0
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public abstract class Auditable {

    /**
     * Fecha y hora de creación del registro.
     * Se establece automáticamente al crear la entidad.
     */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Fecha y hora de última modificación del registro.
     * Se actualiza automáticamente en cada operación de actualización.
     */
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
