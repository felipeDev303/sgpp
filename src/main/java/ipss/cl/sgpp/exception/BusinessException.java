package ipss.cl.sgpp.exception;

/**
 * Excepción para errores de lógica de negocio.
 * Se lanza cuando se violan reglas de negocio específicas de la aplicación.
 * Ejemplos: asignar una práctica a un estudiante que ya tiene una activa,
 * fechas inválidas, operaciones no permitidas según el estado del sistema.
 */
public class BusinessException extends RuntimeException {
    
    private final String errorCode;
    
    /**
     * Constructor con mensaje de error
     * 
     * @param message Descripción del error de negocio
     */
    public BusinessException(String message) {
        super(message);
        this.errorCode = null;
    }
    
    /**
     * Constructor con mensaje y código de error
     * 
     * @param message Descripción del error de negocio
     * @param errorCode Código de error para identificación programática
     */
    public BusinessException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    /**
     * Constructor con mensaje y causa
     * 
     * @param message Descripción del error de negocio
     * @param cause Excepción que causó este error
     */
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = null;
    }
    
    /**
     * Constructor completo
     * 
     * @param message Descripción del error de negocio
     * @param errorCode Código de error
     * @param cause Excepción que causó este error
     */
    public BusinessException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
}
