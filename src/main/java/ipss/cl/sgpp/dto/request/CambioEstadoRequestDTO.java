package ipss.cl.sgpp.dto.request;

import ipss.cl.sgpp.model.EstadoPractica;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO para solicitar el cambio de estado de una práctica.
 */
@Data
public class CambioEstadoRequestDTO {
    
    @NotNull(message = "El nuevo estado es obligatorio")
    private EstadoPractica nuevoEstado;
    
    /**
     * Comentario opcional explicando el motivo del cambio de estado.
     */
    private String comentario;
}
