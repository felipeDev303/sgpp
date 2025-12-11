package ipss.cl.sgpp.dto.request;

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
public class EstudianteRequestDTO {
    
    @NotBlank(message = "El nombre completo es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombreCompleto;
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    @Size(max = 50, message = "El email no puede superar 50 caracteres")
    private String email;
    
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 60, message = "La contraseña debe tener entre 8 y 60 caracteres")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
        message = "La contraseña debe contener al menos una mayúscula, una minúscula y un número"
    )
    private String password;
    
    @NotBlank(message = "El RUT es obligatorio")
    @Pattern(
        regexp = "^\\d{7,8}-[\\dkK]$",
        message = "El RUT debe tener el formato 12345678-9 o 1234567-K"
    )
    private String rut;
    
    @NotBlank(message = "La carrera es obligatoria")
    @Size(min = 3, max = 255, message = "La carrera debe tener entre 3 y 255 caracteres")
    private String carrera;
    
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
