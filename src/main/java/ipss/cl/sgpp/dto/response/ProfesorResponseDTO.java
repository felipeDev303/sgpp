package ipss.cl.sgpp.dto.response;

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
public class ProfesorResponseDTO {
    
    private Long id;
    private String nombreCompleto;
    private String email;
    private String rol;
    private String departamento;
    private String especialidad;
    
    // Información resumida de prácticas supervisadas
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
