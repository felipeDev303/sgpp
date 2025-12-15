package ipss.cl.sgpp.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ipss.cl.sgpp.model.Estudiante;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta para Estudiante.
 * Contiene la información del estudiante sin exponer datos sensibles como la contraseña.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de respuesta con información de un estudiante (sin datos sensibles)")
public class EstudianteResponseDTO {
    
    @Schema(description = "ID único del estudiante", example = "1")
    private Long id;
    
    @Schema(description = "Nombre completo del estudiante", example = "Juan Pérez García")
    private String nombreCompleto;
    
    @Schema(description = "Email institucional del estudiante", example = "juan.perez@ipss.cl")
    private String email;
    
    @Schema(description = "Rol del usuario en el sistema", example = "ESTUDIANTE")
    private String rol;
    
    @Schema(description = "RUT del estudiante", example = "12345678-9")
    private String rut;
    
    @Schema(description = "Carrera que cursa", example = "Ingeniería en Informática")
    private String carrera;
    
    @Schema(description = "Año de ingreso a la institución", example = "2022")
    private Integer anioIngreso;
    
    @Schema(description = "Cantidad de prácticas asociadas al estudiante", example = "2")
    private Integer cantidadPracticas;
    
    /**
     * Método factory para convertir una entidad Estudiante a DTO.
     * 
     * @param estudiante Entidad de estudiante
     * @return DTO con los datos del estudiante
     */
    public static EstudianteResponseDTO from(Estudiante estudiante) {
        if (estudiante == null) {
            return null;
        }
        
        return EstudianteResponseDTO.builder()
                .id(estudiante.getId())
                .nombreCompleto(estudiante.getNombreCompleto())
                .email(estudiante.getEmail())
                .rol(estudiante.getRol())
                .rut(estudiante.getRut())
                .carrera(estudiante.getCarrera())
                .anioIngreso(estudiante.getAnioIngreso())
                .cantidadPracticas(
                    estudiante.getPracticas() != null 
                        ? estudiante.getPracticas().size() 
                        : 0
                )
                .build();
    }
    
    /**
     * Método factory simplificado sin cargar la lista de prácticas.
     * Útil cuando no se necesita la cantidad de prácticas para evitar lazy loading.
     * 
     * @param estudiante Entidad de estudiante
     * @return DTO con los datos del estudiante sin información de prácticas
     */
    public static EstudianteResponseDTO fromWithoutPracticas(Estudiante estudiante) {
        if (estudiante == null) {
            return null;
        }
        
        return EstudianteResponseDTO.builder()
                .id(estudiante.getId())
                .nombreCompleto(estudiante.getNombreCompleto())
                .email(estudiante.getEmail())
                .rol(estudiante.getRol())
                .rut(estudiante.getRut())
                .carrera(estudiante.getCarrera())
                .anioIngreso(estudiante.getAnioIngreso())
                .cantidadPracticas(0)
                .build();
    }
}
