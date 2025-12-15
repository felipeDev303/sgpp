package ipss.cl.sgpp.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para crear o actualizar un Profesor.
 * Contiene validaciones de Bean Validation para garantizar la integridad de los datos.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para crear o actualizar información de un profesor")
public class ProfesorRequestDTO {
    
    @Schema(description = "Nombre completo del profesor", example = "María González López", required = true, minLength = 3, maxLength = 100)
    @NotBlank(message = "El nombre completo es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombreCompleto;
    
    @Schema(description = "Correo electrónico institucional del profesor", example = "maria.gonzalez@ipss.cl", required = true, maxLength = 50)
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    @Size(max = 50, message = "El email no puede superar 50 caracteres")
    private String email;
    
    @Schema(description = "Contraseña del profesor (debe contener mayúsculas, minúsculas y números)", example = "Password123", required = true, minLength = 8, maxLength = 60)
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 60, message = "La contraseña debe tener entre 8 y 60 caracteres")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
        message = "La contraseña debe contener al menos una mayúscula, una minúscula y un número"
    )
    private String password;
    
    @Schema(description = "Departamento al que pertenece el profesor", example = "Departamento de Informática", maxLength = 255)
    @Size(max = 255, message = "El departamento no puede superar 255 caracteres")
    private String departamento;
    
    @Schema(description = "Especialidad o área de expertise del profesor", example = "Desarrollo de Software", maxLength = 255)
    @Size(max = 255, message = "La especialidad no puede superar 255 caracteres")
    private String especialidad;
}
