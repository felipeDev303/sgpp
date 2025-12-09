package ipss.cl.sgpp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true) // Necesario para Lombok en herencia
public class Estudiante extends Usuario {

    private String carrera; // Carrera del estudiante 

    // Un estudiante puede tener múltiples prácticas (historial)
    @OneToMany(mappedBy = "estudiante")
    private List<Practica> practicas;
}