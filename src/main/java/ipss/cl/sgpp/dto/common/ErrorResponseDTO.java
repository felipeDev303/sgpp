package ipss.cl.sgpp.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de respuesta estándar para errores de la API")
public class ErrorResponseDTO {
    
    @Schema(description = "Marca de tiempo cuando ocurrió el error", example = "2025-12-11T10:30:00")
    private LocalDateTime timestamp;
    
    @Schema(description = "Código de estado HTTP", example = "404")
    private int status;
    
    @Schema(description = "Nombre del error HTTP", example = "Not Found")
    private String error;
    
    @Schema(description = "Mensaje descriptivo del error", example = "Práctica no encontrada con ID: 999")
    private String message;
    
    @Schema(description = "Ruta del endpoint que generó el error", example = "/api/v1/practicas/999")
    private String path;
    
    @Schema(description = "Detalles adicionales del error (por ejemplo, errores de validación por campo)")
    @Builder.Default
    private Map<String, String> details = new HashMap<>();
    
    /**
     * Agrega un detalle adicional al error.
     * 
     * @param key Clave del detalle
     * @param value Valor del detalle
     */
    public void addDetail(String key, String value) {
        if (this.details == null) {
            this.details = new HashMap<>();
        }
        this.details.put(key, value);
    }
}
