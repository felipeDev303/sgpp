package ipss.cl.sgpp.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "DTO para crear o actualizar información de un jefe directo (supervisor en empresa)")
public class JefeDirectoRequestDTO {
    
    @Schema(description = "Nombre completo del jefe directo o supervisor", example = "Carlos Ramírez Soto", required = true, minLength = 3, maxLength = 100)
    @NotBlank(message = "El nombre del jefe directo es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombre;
    
    @Schema(description = "Información de contacto (teléfono, email, etc.)", example = "+56 9 1234 5678", maxLength = 100)
    @Size(max = 100, message = "El contacto no puede superar 100 caracteres")
    private String contacto;
}
