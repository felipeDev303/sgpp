package ipss.cl.sgpp.model;

/**
 * Enum que representa los diferentes estados de una práctica profesional.
 * 
 * Simplificado a 3 estados básicos según el flujo natural de una práctica:
 * PENDIENTE → EN_CURSO → COMPLETADA
 * 
 * Nota: Estados APROBADA, RECHAZADA y SUSPENDIDA fueron removidos ya que la
 * problemática no menciona flujos de aprobación formal ni suspensión temporal.
 * El foco está en el ciclo de vida básico de una práctica profesional.
 * 
 * @author SGPP Team
 * @since 1.0
 */
public enum EstadoPractica {
    /**
     * La práctica ha sido registrada y está pendiente de iniciar.
     * Estado inicial cuando se crea una nueva práctica.
     */
    PENDIENTE("Pendiente de inicio"),
    
    /**
     * La práctica está en curso, el estudiante está realizando las actividades.
     * Estado activo durante el desarrollo de la práctica.
     */
    EN_CURSO("En curso"),
    
    /**
     * La práctica ha sido completada exitosamente.
     * Estado terminal, no permite más cambios.
     */
    COMPLETADA("Completada");
    
    private final String descripcion;
    
    EstadoPractica(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    /**
     * Verifica si el estado es terminal (no permite más cambios).
     * Solo COMPLETADA es un estado terminal en el flujo simplificado.
     */
    public boolean esEstadoTerminal() {
        return this == COMPLETADA;
    }
    
    /**
     * Verifica si el estado permite modificaciones a la práctica.
     * Solo estados no terminales permiten cambios.
     */
    public boolean permiteModificaciones() {
        return this == PENDIENTE || this == EN_CURSO;
    }
}
