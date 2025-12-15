package ipss.cl.sgpp.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import ipss.cl.sgpp.model.EstadoPractica;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO para solicitar el cambio de estado de una práctica.
 */
@Data
@Schema(description = "DTO para solicitar un cambio de estado en una práctica profesional")
public class CambioEstadoRequestDTO {
    
    @Schema(description = "Nuevo estado al que se cambiará la práctica",
            example = "EN_CURSO",
            required = true,
            allowableValues = {"PENDIENTE", "EN_CURSO", "COMPLETADA"})
    @NotNull(message = "El nuevo estado es obligatorio")
    private EstadoPractica nuevoEstado;
    
    @Schema(description = "Comentario opcional explicando el motivo del cambio de estado",
            example = "La práctica inició según lo planificado")
    private String comentario;
}
