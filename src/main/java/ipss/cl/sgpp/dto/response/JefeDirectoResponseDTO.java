package ipss.cl.sgpp.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ipss.cl.sgpp.model.JefeDirecto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta para Jefe Directo.
 * Contiene la información del supervisor de empresa.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de respuesta con información de un jefe directo (supervisor en empresa)")
public class JefeDirectoResponseDTO {
    
    @Schema(description = "ID único del jefe directo", example = "1")
    private Long id;
    
    @Schema(description = "Nombre completo del jefe directo", example = "Carlos Ramírez Soto")
    private String nombre;
    
    @Schema(description = "Información de contacto", example = "+56 9 1234 5678")
    private String contacto;
    
    @Schema(description = "Cantidad de prácticas supervisadas", example = "3")
    private Integer cantidadPracticasSupervisadas;
    
    /**
     * Método factory para convertir una entidad JefeDirecto a DTO.
     * 
     * @param jefeDirecto Entidad de jefe directo
     * @return DTO con los datos del jefe directo
     */
    public static JefeDirectoResponseDTO from(JefeDirecto jefeDirecto) {
        if (jefeDirecto == null) {
            return null;
        }
        
        return JefeDirectoResponseDTO.builder()
                .id(jefeDirecto.getId())
                .nombre(jefeDirecto.getNombre())
                .contacto(jefeDirecto.getContacto())
                .cantidadPracticasSupervisadas(
                    jefeDirecto.getPracticasSupervisadas() != null 
                        ? jefeDirecto.getPracticasSupervisadas().size() 
                        : 0
                )
                .build();
    }
    
    /**
     * Método factory simplificado sin cargar la lista de prácticas supervisadas.
     * Útil cuando no se necesita la cantidad de prácticas para evitar lazy loading.
     * 
     * @param jefeDirecto Entidad de jefe directo
     * @return DTO con los datos del jefe directo sin información de prácticas
     */
    public static JefeDirectoResponseDTO fromWithoutPracticas(JefeDirecto jefeDirecto) {
        if (jefeDirecto == null) {
            return null;
        }
        
        return JefeDirectoResponseDTO.builder()
                .id(jefeDirecto.getId())
                .nombre(jefeDirecto.getNombre())
                .contacto(jefeDirecto.getContacto())
                .cantidadPracticasSupervisadas(0)
                .build();
    }
}
