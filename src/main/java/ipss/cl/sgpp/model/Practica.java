package ipss.cl.sgpp.model;

import ipss.cl.sgpp.validation.FechasCoherentes;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FechasCoherentes(
    fechaInicio = "fechaInicio", 
    fechaTermino = "fechaTermino",
    message = "La fecha de término debe ser posterior a la fecha de inicio"
)
public class Practica extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Asegura que la fecha de inicio no sea nula y sea en el presente o futuro
    @NotNull(message = "La fecha de inicio es obligatoria")
    @FutureOrPresent(message = "La fecha de inicio no puede ser en el pasado")
    private LocalDate fechaInicio; 

    @NotNull(message = "La fecha de término es obligatoria")
    private LocalDate fechaTermino;

    @NotBlank(message = "La descripción de actividades es obligatoria")
    @Column(columnDefinition = "TEXT")
    private String descripcionActividades;
    
    @NotNull(message = "El estado de la práctica es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private EstadoPractica estado = EstadoPractica.PENDIENTE;
    
    // Validamos que los objetos relacionados (IDs) no sean nulos
    @NotNull(message = "El estudiante es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profesor_id")
    private Profesor profesor;
    
    @NotNull(message = "La empresa es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @NotNull(message = "El jefe directo es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jefe_directo_id", nullable = false)
    private JefeDirecto jefeDirecto;
    
    /**
     * Verifica si la práctica puede ser modificada según su estado actual.
     */
    public boolean puedeSerModificada() {
        return estado != null && estado.permiteModificaciones();
    }
    
    /**
     * Verifica si la práctica está en un estado terminal.
     */
    public boolean estaEnEstadoTerminal() {
        return estado != null && estado.esEstadoTerminal();
    }
    
    /**
     * Cambia el estado de la práctica validando las transiciones permitidas.
     * 
     * @param nuevoEstado El nuevo estado a asignar
     * @return true si el cambio fue exitoso, false si no está permitido
     */
    public boolean cambiarEstado(EstadoPractica nuevoEstado) {
        if (nuevoEstado == null) {
            return false;
        }
        
        // No permitir cambios desde estados terminales
        if (estaEnEstadoTerminal()) {
            return false;
        }
        
        // Validar transiciones permitidas
        boolean transicionPermitida = esTransicionValida(this.estado, nuevoEstado);
        
        if (transicionPermitida) {
            this.estado = nuevoEstado;
        }
        
        return transicionPermitida;
    }
    
    /**
     * Valida si una transición de estado es válida según las reglas de negocio.
     */
    private boolean esTransicionValida(EstadoPractica estadoActual, EstadoPractica estadoNuevo) {
        if (estadoActual == estadoNuevo) {
            return true; // Permitir mantener el mismo estado
        }
        
        return switch (estadoActual) {
            case PENDIENTE -> estadoNuevo == EstadoPractica.APROBADA || 
                             estadoNuevo == EstadoPractica.RECHAZADA;
                             
            case APROBADA -> estadoNuevo == EstadoPractica.EN_CURSO || 
                            estadoNuevo == EstadoPractica.RECHAZADA;
                            
            case EN_CURSO -> estadoNuevo == EstadoPractica.COMPLETADA || 
                            estadoNuevo == EstadoPractica.SUSPENDIDA ||
                            estadoNuevo == EstadoPractica.RECHAZADA;
                            
            case SUSPENDIDA -> estadoNuevo == EstadoPractica.EN_CURSO || 
                              estadoNuevo == EstadoPractica.RECHAZADA;
                              
            default -> false; // Estados terminales no permiten transiciones
        };
    }
}