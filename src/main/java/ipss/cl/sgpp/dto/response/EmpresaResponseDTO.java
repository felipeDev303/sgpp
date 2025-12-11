package ipss.cl.sgpp.dto.response;

import ipss.cl.sgpp.model.Empresa;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta para Empresa.
 * Contiene la información de la empresa sin exponer datos sensibles.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaResponseDTO {
    
    private Long id;
    private String nombre;
    private String rut;
    private String direccion;
    private String contactoEmail;
    
    // Información resumida de prácticas asociadas
    private Integer cantidadPracticas;
    
    /**
     * Método factory para convertir una entidad Empresa a DTO.
     * 
     * @param empresa Entidad de empresa
     * @return DTO con los datos de la empresa
     */
    public static EmpresaResponseDTO from(Empresa empresa) {
        if (empresa == null) {
            return null;
        }
        
        return EmpresaResponseDTO.builder()
                .id(empresa.getId())
                .nombre(empresa.getNombre())
                .rut(empresa.getRut())
                .direccion(empresa.getDireccion())
                .contactoEmail(empresa.getContactoEmail())
                .cantidadPracticas(
                    empresa.getPracticas() != null 
                        ? empresa.getPracticas().size() 
                        : 0
                )
                .build();
    }
    
    /**
     * Método factory simplificado sin cargar la lista de prácticas.
     * Útil cuando no se necesita la cantidad de prácticas para evitar lazy loading.
     * 
     * @param empresa Entidad de empresa
     * @return DTO con los datos de la empresa sin información de prácticas
     */
    public static EmpresaResponseDTO fromWithoutPracticas(Empresa empresa) {
        if (empresa == null) {
            return null;
        }
        
        return EmpresaResponseDTO.builder()
                .id(empresa.getId())
                .nombre(empresa.getNombre())
                .rut(empresa.getRut())
                .direccion(empresa.getDireccion())
                .contactoEmail(empresa.getContactoEmail())
                .cantidadPracticas(0)
                .build();
    }
}
