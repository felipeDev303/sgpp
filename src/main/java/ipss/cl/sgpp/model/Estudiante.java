package ipss.cl.sgpp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true) // Necesario para Lombok en herencia
public class Estudiante extends Usuario {

    @NotBlank(message = "El RUT es obligatorio")
    @Pattern(regexp = "^\\d{7,8}-[\\dkK]$", message = "El RUT debe tener el formato 12345678-9 o 1234567-K")
    @Column(nullable = false, unique = true, length = 12)
    private String rut;

    @NotBlank(message = "La carrera es obligatoria")
    @Column(nullable = false)
    private String carrera;

    @NotNull(message = "El año de ingreso es obligatorio")
    @Positive(message = "El año de ingreso debe ser un número positivo")
    @Column(name = "anio_ingreso", nullable = false)
    private Integer anioIngreso;

    // Un estudiante puede tener múltiples prácticas (historial)
    @OneToMany(mappedBy = "estudiante")
    private List<Practica> practicas;
}