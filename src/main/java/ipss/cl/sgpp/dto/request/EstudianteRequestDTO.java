package ipss.cl.sgpp.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Year;

/**
 * DTO para crear o actualizar un Estudiante.
 * Contiene validaciones de Bean Validation para garantizar la integridad de los datos.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para crear o actualizar información de un estudiante")
public class EstudianteRequestDTO {
    
    @Schema(description = "Nombre completo del estudiante", example = "Juan Pérez García", required = true, minLength = 3, maxLength = 100)
    @NotBlank(message = "El nombre completo es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombreCompleto;
    
    @Schema(description = "Correo electrónico institucional del estudiante", example = "juan.perez@ipss.cl", required = true, maxLength = 50)
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    @Size(max = 50, message = "El email no puede superar 50 caracteres")
    private String email;
    
    @Schema(description = "Contraseña del estudiante (debe contener mayúsculas, minúsculas y números)", example = "Password123", required = true, minLength = 8, maxLength = 60)
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 60, message = "La contraseña debe tener entre 8 y 60 caracteres")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
        message = "La contraseña debe contener al menos una mayúscula, una minúscula y un número"
    )
    private String password;
    
    @Schema(description = "RUT del estudiante (formato chileno)", example = "12345678-9", required = true)
    @NotBlank(message = "El RUT es obligatorio")
    @Pattern(
        regexp = "^\\d{7,8}-[\\dkK]$",
        message = "El RUT debe tener el formato 12345678-9 o 1234567-K"
    )
    private String rut;
    
    @Schema(description = "Carrera que cursa el estudiante", example = "Ingeniería en Informática", required = true, minLength = 3, maxLength = 255)
    @NotBlank(message = "La carrera es obligatoria")
    @Size(min = 3, max = 255, message = "La carrera debe tener entre 3 y 255 caracteres")
    private String carrera;
    
    @Schema(description = "Año de ingreso a la institución", example = "2022", required = true, minimum = "2000", maximum = "2100")
    @NotNull(message = "El año de ingreso es obligatorio")
    @Min(value = 2000, message = "El año de ingreso debe ser posterior al año 2000")
    @Max(value = 2100, message = "El año de ingreso no puede ser posterior a 2100")
    private Integer anioIngreso;
    
    /**
     * Valida que el año de ingreso no sea futuro.
     * 
     * @return true si el año de ingreso es válido
     */
    @AssertTrue(message = "El año de ingreso no puede ser posterior al año actual")
    public boolean isAnioIngresoValido() {
        if (anioIngreso == null) {
            return true; // Será validado por @NotNull
        }
        return anioIngreso <= Year.now().getValue();
    }
}
