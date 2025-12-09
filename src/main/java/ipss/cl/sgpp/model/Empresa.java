package ipss.cl.sgpp.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre; // Nombre de la empresa 

    private String direccion; // Dirección de la empresa 

    private String telefono; // Teléfono de la empresa 

    @OneToMany(mappedBy = "empresa")
    private List<Practica> practicas;
}