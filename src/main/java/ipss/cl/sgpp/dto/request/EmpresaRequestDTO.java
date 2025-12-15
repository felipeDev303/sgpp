package ipss.cl.sgpp.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para crear o actualizar una Empresa.
 * Contiene validaciones de Bean Validation para garantizar la integridad de los datos.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para crear o actualizar información de una empresa")
public class EmpresaRequestDTO {
    
    @Schema(description = "Nombre o razón social de la empresa", example = "Tech Solutions SpA", required = true, minLength = 3, maxLength = 255)
    @NotBlank(message = "El nombre de la empresa es obligatorio")
    @Size(min = 3, max = 255, message = "El nombre debe tener entre 3 y 255 caracteres")
    private String nombre;
    
    @Schema(description = "RUT de la empresa (formato chileno)", example = "76123456-7", required = true)
    @NotBlank(message = "El RUT de la empresa es obligatorio")
    @Pattern(
        regexp = "^\\d{7,8}-[\\dkK]$",
        message = "El RUT debe tener el formato 12345678-9 o 1234567-K"
    )
    private String rut;
    
    @Schema(description = "Dirección física de la empresa", example = "Av. Providencia 1234, Santiago", maxLength = 255)
    @Size(max = 255, message = "La dirección no puede superar 255 caracteres")
    private String direccion;
    
    @Schema(description = "Email de contacto de la empresa", example = "contacto@techsolutions.cl", maxLength = 255)
    @Email(message = "El email de contacto debe tener un formato válido")
    @Size(max = 255, message = "El email no puede superar 255 caracteres")
    private String contactoEmail;
}
