package ipss.cl.sgpp.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Inheritance(strategy = InheritanceType.JOINED) // Estrategia de Herencia JOINED
@Data // De Lombok para getters, setters, etc.
public abstract class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombreCompleto;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false, length = 60)
    private String password;

    // Podrías usar un Enum para el rol, pero lo dejamos simple por ahora
    @Column(nullable = false, length = 20)
    private String rol; // "ESTUDIANTE" o "PROFESOR"
}