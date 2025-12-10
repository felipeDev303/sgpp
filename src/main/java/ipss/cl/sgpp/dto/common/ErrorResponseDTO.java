package ipss.cl.sgpp.dto.common;

import lombok.Data;
import lombok.Builder;

import java.time.LocalDate;

@Data
@Builder
public class ErrorResponseDTO {
    private LocalDate timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

}
