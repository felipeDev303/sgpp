package ipss.cl.sgpp.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Clase base auditable que proporciona campos de auditoría automáticos.
 * Las entidades que extiendan esta clase tendrán tracking automático de:
 * - Fecha de creación (createdAt)
 * - Fecha de última modificación (updatedAt)
 * - Usuario que creó (createdBy)
 * - Usuario que modificó (updatedBy)
 * 
 * Los valores se establecen automáticamente mediante JPA Auditing.
 * 
 * @author SGPP Team
 * @since 1.0
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
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

    /**
     * Usuario que creó el registro (email).
     * Se establece automáticamente al crear la entidad.
     */
    @CreatedBy
    @Column(name = "created_by", length = 100, updatable = false)
    private String createdBy;

    /**
     * Usuario que modificó el registro por última vez (email).
     * Se actualiza automáticamente en cada operación de actualización.
     */
    @LastModifiedBy
    @Column(name = "updated_by", length = 100)
    private String updatedBy;
}
