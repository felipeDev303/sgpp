package ipss.cl.sgpp.dto.common;

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
public class ErrorResponseDTO {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    
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
