package ipss.cl.sgpp.dto.request;

import lombok.Data;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Data
public class PracticaRequestDTO {
    
    @NotNull(message = "El ID del estudiante es obligatorio")
    @Positive(message = "El ID del estudiante debe ser un número positivo")
    private Long estudianteId;

    @Positive(message = "El ID del profesor debe ser un número positivo")
    private Long profesorSupervisorId; // Opcional

    @NotNull(message = "El ID de la empresa es obligatorio")
    @Positive(message = "El ID de la empresa debe ser un número positivo")
    private Long empresaId;

    @NotNull(message = "El ID del jefe directo es obligatorio")
    @Positive(message = "El ID del jefe directo debe ser un número positivo")
    private Long jefeDirectoId;

    @NotNull(message = "La fecha de inicio es obligatoria")
    @FutureOrPresent(message = "La fecha de inicio no puede ser en el pasado")
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de término es obligatoria")
    @Future(message = "La fecha de término debe ser futura")
    private LocalDate fechaTermino;

    @NotBlank(message = "La descripción de actividades es obligatoria")
    @Size(max = 1000, message = "La descripción no puede superar los 1000 caracteres")
    private String descripcionActividades;

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
