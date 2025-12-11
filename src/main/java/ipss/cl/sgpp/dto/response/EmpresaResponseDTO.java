package ipss.cl.sgpp.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "DTO de respuesta con información de una empresa")
public class EmpresaResponseDTO {
    
    @Schema(description = "ID único de la empresa", example = "1")
    private Long id;
    
    @Schema(description = "Nombre o razón social de la empresa", example = "Tech Solutions SpA")
    private String nombre;
    
    @Schema(description = "RUT de la empresa", example = "76123456-7")
    private String rut;
    
    @Schema(description = "Dirección física de la empresa", example = "Av. Providencia 1234, Santiago")
    private String direccion;
    
    @Schema(description = "Email de contacto de la empresa", example = "contacto@techsolutions.cl")
    private String contactoEmail;
    
    @Schema(description = "Cantidad de prácticas asociadas a esta empresa", example = "10")
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
