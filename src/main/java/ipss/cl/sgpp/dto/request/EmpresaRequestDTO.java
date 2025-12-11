package ipss.cl.sgpp.dto.request;

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
public class EmpresaRequestDTO {
    
    @NotBlank(message = "El nombre de la empresa es obligatorio")
    @Size(min = 3, max = 255, message = "El nombre debe tener entre 3 y 255 caracteres")
    private String nombre;
    
    @NotBlank(message = "El RUT de la empresa es obligatorio")
    @Pattern(
        regexp = "^\\d{7,8}-[\\dkK]$",
        message = "El RUT debe tener el formato 12345678-9 o 1234567-K"
    )
    private String rut;
    
    @Size(max = 255, message = "La dirección no puede superar 255 caracteres")
    private String direccion;
    
    @Email(message = "El email de contacto debe tener un formato válido")
    @Size(max = 255, message = "El email no puede superar 255 caracteres")
    private String contactoEmail;
}
