package ipss.cl.sgpp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para crear o actualizar un Jefe Directo (supervisor de empresa).
 * Contiene validaciones de Bean Validation para garantizar la integridad de los datos.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JefeDirectoRequestDTO {
    
    @NotBlank(message = "El nombre del jefe directo es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombre;
    
    @Size(max = 100, message = "El contacto no puede superar 100 caracteres")
    private String contacto;
}
