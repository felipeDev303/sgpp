package ipss.cl.sgpp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Profesor extends Usuario {

    // Otros campos específicos del profesor si son necesarios (ej. departamento)

    // Un profesor puede supervisar múltiples prácticas 
    @OneToMany(mappedBy = "profesorSupervisor")
    private List<Practica> practicasSupervisadas;
}