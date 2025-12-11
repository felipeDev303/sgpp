package ipss.cl.sgpp.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ipss.cl.sgpp.model.Profesor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta para Profesor.
 * Contiene la información del profesor sin exponer datos sensibles como la contraseña.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de respuesta con información de un profesor (sin datos sensibles)")
public class ProfesorResponseDTO {
    
    @Schema(description = "ID único del profesor", example = "1")
    private Long id;
    
    @Schema(description = "Nombre completo del profesor", example = "María González López")
    private String nombreCompleto;
    
    @Schema(description = "Email institucional del profesor", example = "maria.gonzalez@ipss.cl")
    private String email;
    
    @Schema(description = "Rol del usuario en el sistema", example = "PROFESOR")
    private String rol;
    
    @Schema(description = "Departamento al que pertenece", example = "Departamento de Informática")
    private String departamento;
    
    @Schema(description = "Especialidad o área de expertise", example = "Desarrollo de Software")
    private String especialidad;
    
    @Schema(description = "Cantidad de prácticas supervisadas por el profesor", example = "5")
    private Integer cantidadPracticasSupervisadas;
    
    /**
     * Método factory para convertir una entidad Profesor a DTO.
     * 
     * @param profesor Entidad de profesor
     * @return DTO con los datos del profesor
     */
    public static ProfesorResponseDTO from(Profesor profesor) {
        if (profesor == null) {
            return null;
        }
        
        return ProfesorResponseDTO.builder()
                .id(profesor.getId())
                .nombreCompleto(profesor.getNombreCompleto())
                .email(profesor.getEmail())
                .rol(profesor.getRol())
                .departamento(profesor.getDepartamento())
                .especialidad(profesor.getEspecialidad())
                .cantidadPracticasSupervisadas(
                    profesor.getPracticasSupervisadas() != null 
                        ? profesor.getPracticasSupervisadas().size() 
                        : 0
                )
                .build();
    }
    
    /**
     * Método factory simplificado sin cargar la lista de prácticas supervisadas.
     * Útil cuando no se necesita la cantidad de prácticas para evitar lazy loading.
     * 
     * @param profesor Entidad de profesor
     * @return DTO con los datos del profesor sin información de prácticas
     */
    public static ProfesorResponseDTO fromWithoutPracticas(Profesor profesor) {
        if (profesor == null) {
            return null;
        }
        
        return ProfesorResponseDTO.builder()
                .id(profesor.getId())
                .nombreCompleto(profesor.getNombreCompleto())
                .email(profesor.getEmail())
                .rol(profesor.getRol())
                .departamento(profesor.getDepartamento())
                .especialidad(profesor.getEspecialidad())
                .cantidadPracticasSupervisadas(0)
                .build();
    }
}
