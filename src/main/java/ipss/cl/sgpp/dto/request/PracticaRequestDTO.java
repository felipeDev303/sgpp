package ipss.cl.sgpp.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import ipss.cl.sgpp.model.EstadoPractica;
import lombok.Data;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Data
@Schema(description = "DTO para crear o actualizar una práctica profesional")
public class PracticaRequestDTO {
    
    @Schema(description = "ID del estudiante que realizará la práctica", example = "1", required = true)
    @NotNull(message = "El ID del estudiante es obligatorio")
    @Positive(message = "El ID del estudiante debe ser un número positivo")
    private Long estudianteId;

    @Schema(description = "ID del profesor guía (opcional)", example = "2")
    @Positive(message = "El ID del profesor debe ser un número positivo")
    private Long profesorId; // Opcional

    @Schema(description = "ID de la empresa donde se realizará la práctica", example = "1", required = true)
    @NotNull(message = "El ID de la empresa es obligatorio")
    @Positive(message = "El ID de la empresa debe ser un número positivo")
    private Long empresaId;

    @Schema(description = "ID del jefe directo (supervisor en la empresa)", example = "1", required = true)
    @NotNull(message = "El ID del jefe directo es obligatorio")
    @Positive(message = "El ID del jefe directo debe ser un número positivo")
    private Long jefeDirectoId;

    @Schema(description = "Fecha de inicio de la práctica", example = "2025-01-15", required = true)
    @NotNull(message = "La fecha de inicio es obligatoria")
    @FutureOrPresent(message = "La fecha de inicio no puede ser en el pasado")
    private LocalDate fechaInicio;

    @Schema(description = "Fecha de término de la práctica", example = "2025-06-30", required = true)
    @NotNull(message = "La fecha de término es obligatoria")
    @Future(message = "La fecha de término debe ser futura")
    private LocalDate fechaTermino;

    @Schema(description = "Descripción de las actividades a realizar durante la práctica", 
            example = "Desarrollo de aplicaciones web con Spring Boot y React",
            required = true,
            maxLength = 1000)
    @NotBlank(message = "La descripción de actividades es obligatoria")
    @Size(max = 1000, message = "La descripción no puede superar los 1000 caracteres")
    private String descripcionActividades;
    
    @Schema(description = "Estado inicial de la práctica (PENDIENTE por defecto si no se especifica)",
            example = "PENDIENTE",
            allowableValues = {"PENDIENTE", "EN_CURSO", "COMPLETADA"})
    private EstadoPractica estado;

    /**
     * Validación personalizada para asegurar que fechaTermino > fechaInicio
     */
    @AssertTrue(message = "La fecha de término debe ser posterior a la fecha de inicio")
    public boolean isFechasValidas() {
        if (fechaInicio == null || fechaTermino == null) {
            return true; // Dejar que @NotNull maneje los nulls
        }
        return fechaTermino.isAfter(fechaInicio);
    }
}
