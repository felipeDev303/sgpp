package ipss.cl.sgpp.dto.response;

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
public class EstudianteResponseDTO {
    
    private Long id;
    private String nombreCompleto;
    private String email;
    private String rol;
    private String rut;
    private String carrera;
    private Integer anioIngreso;
    
    // Información resumida de prácticas
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
