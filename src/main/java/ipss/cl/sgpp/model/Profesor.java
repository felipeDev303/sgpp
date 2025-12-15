package ipss.cl.sgpp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Profesor extends Usuario {

    @Size(max = 255, message = "El departamento no puede superar 255 caracteres")
    @Column(length = 255)
    private String departamento;

    @Size(max = 255, message = "La especialidad no puede superar 255 caracteres")
    @Column(length = 255)
    private String especialidad;

    // Un profesor puede supervisar múltiples prácticas 
    @OneToMany(mappedBy = "profesor")
    private List<Practica> practicasSupervisadas;
}