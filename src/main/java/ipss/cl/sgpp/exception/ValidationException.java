package ipss.cl.sgpp.exception;

import java.util.HashMap;
import java.util.Map;

/**
 * Excepción para errores de validación de datos.
 * Se lanza cuando los datos de entrada no cumplen con las reglas de validación.
 * Puede contener múltiples errores de validación por campo.
 */
public class ValidationException extends RuntimeException {
    
    private final Map<String, String> errors;
    
    /**
     * Constructor con mensaje simple
     * 
     * @param message Mensaje de error de validación
     */
    public ValidationException(String message) {
        super(message);
        this.errors = new HashMap<>();
    }
    
    /**
     * Constructor con mapa de errores por campo
     * 
     * @param message Mensaje general de error
     * @param errors Mapa de errores donde la clave es el nombre del campo
     *               y el valor es el mensaje de error específico
     */
    public ValidationException(String message, Map<String, String> errors) {
        super(message);
        this.errors = errors != null ? new HashMap<>(errors) : new HashMap<>();
    }
    
    /**
     * Constructor con error de un solo campo
     * 
     * @param message Mensaje general de error
     * @param fieldName Nombre del campo con error
     * @param fieldError Mensaje de error específico del campo
     */
    public ValidationException(String message, String fieldName, String fieldError) {
        super(message);
        this.errors = new HashMap<>();
        this.errors.put(fieldName, fieldError);
    }
    
    /**
     * Agrega un error de validación para un campo específico
     * 
     * @param fieldName Nombre del campo
     * @param fieldError Mensaje de error
     */
    public void addError(String fieldName, String fieldError) {
        this.errors.put(fieldName, fieldError);
    }
    
    /**
     * Obtiene todos los errores de validación
     * 
     * @return Mapa inmutable de errores por campo
     */
    public Map<String, String> getErrors() {
        return new HashMap<>(errors);
    }
    
    /**
     * Verifica si hay errores de validación
     * 
     * @return true si hay al menos un error
     */
    public boolean hasErrors() {
        return !errors.isEmpty();
    }
}
