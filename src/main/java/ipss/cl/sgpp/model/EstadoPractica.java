package ipss.cl.sgpp.model;

/**
 * Enum que representa los diferentes estados de una práctica profesional.
 */
public enum EstadoPractica {
    /**
     * La práctica ha sido registrada pero aún no ha sido aprobada por el profesor.
     */
    PENDIENTE("Pendiente de aprobación"),
    
    /**
     * La práctica ha sido aprobada y el estudiante puede comenzar.
     */
    APROBADA("Aprobada"),
    
    /**
     * La práctica está en curso, el estudiante está realizando las actividades.
     */
    EN_CURSO("En curso"),
    
    /**
     * La práctica ha sido completada exitosamente.
     */
    COMPLETADA("Completada"),
    
    /**
     * La práctica ha sido rechazada o cancelada.
     */
    RECHAZADA("Rechazada"),
    
    /**
     * La práctica fue suspendida temporalmente.
     */
    SUSPENDIDA("Suspendida");
    
    private final String descripcion;
    
    EstadoPractica(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    /**
     * Verifica si el estado es terminal (no permite más cambios).
     */
    public boolean esEstadoTerminal() {
        return this == COMPLETADA || this == RECHAZADA;
    }
    
    /**
     * Verifica si el estado permite modificaciones.
     */
    public boolean permiteModificaciones() {
        return this == PENDIENTE || this == APROBADA || this == EN_CURSO;
    }
}
