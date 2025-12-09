package ipss.cl.sgpp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent; // Para fechas
import jakarta.validation.constraints.NotBlank; // Para Strings
import jakarta.validation.constraints.NotNull; // Para IDs y objetos
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
public class Practica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Asegura que la fecha de inicio no sea nula y sea en el presente o futuro
    @NotNull(message = "La fecha de inicio es obligatoria")
    @FutureOrPresent(message = "La fecha de inicio no puede ser en el pasado")
    private LocalDate fechaInicio; 

    @NotNull(message = "La fecha de término es obligatoria")
    private LocalDate fechaTermino; // Validación de la lógica (inicio antes de término) se hace en el Service/DTO.

    @NotBlank(message = "La descripción de actividades es obligatoria")
    @Column(columnDefinition = "TEXT")
    private String descripcionActividades;
    
    // Validamos que los objetos relacionados (IDs) no sean nulos
    @NotNull(message = "El estudiante es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profesor_id")
    private Profesor profesorSupervisor;
    
    @NotNull(message = "La empresa es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @NotNull(message = "El jefe directo es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jefe_directo_id", nullable = false)
    private JefeDirecto jefeDirecto;
}