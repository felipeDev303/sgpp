package ipss.cl.sgpp.exception;

/**
 * Excepción lanzada cuando un recurso solicitado no se encuentra en la base de datos.
 * Utilizada para entidades como Estudiante, Profesor, Empresa, Practica, etc.
 */
public class ResourceNotFoundException extends RuntimeException {
    
    private final String resourceName;
    private final String fieldName;
    private final Object fieldValue;
    
    /**
     * Constructor con mensaje personalizado
     * 
     * @param message Mensaje de error
     */
    public ResourceNotFoundException(String message) {
        super(message);
        this.resourceName = null;
        this.fieldName = null;
        this.fieldValue = null;
    }
    
    /**
     * Constructor con información estructurada del recurso no encontrado
     * 
     * @param resourceName Nombre del recurso (ej: "Estudiante", "Practica")
     * @param fieldName Nombre del campo usado para buscar (ej: "id", "rut")
     * @param fieldValue Valor del campo buscado
     */
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s no encontrado con %s: '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
    
    public String getResourceName() {
        return resourceName;
    }
    
    public String getFieldName() {
        return fieldName;
    }
    
    public Object getFieldValue() {
        return fieldValue;
    }
}
