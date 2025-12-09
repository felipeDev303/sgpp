package ipss.cl.sgpp.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
public class Practica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate fechaInicio; // Fecha de inicio de la práctica 

    private LocalDate fechaTermino; // Fecha de término de la práctica 

    @Column(columnDefinition = "TEXT")
    private String descripcionActividades; // Descripción de las actividades 
    
    // Relación ManyToOne con Estudiante (El FK de Estudiante va aquí)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;

    // Relación ManyToOne con Profesor Supervisor (El FK del Profesor va aquí)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profesor_id")
    private Profesor profesorSupervisor;
    
    // Relación ManyToOne con Empresa
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    // Relación ManyToOne con Jefe Directo (Supervisor de la empresa)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jefe_directo_id", nullable = false)
    private JefeDirecto jefeDirecto;
}