package ipss.cl.sgpp.exception;

import ipss.cl.sgpp.dto.common.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Manejador global de excepciones para la aplicación.
 * Captura y procesa todas las excepciones lanzadas por los controladores,
 * convirtiendo las en respuestas HTTP estandarizadas.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Maneja excepciones cuando un recurso no es encontrado.
     * 
     * @param ex ResourceNotFoundException lanzada
     * @param request HttpServletRequest para obtener la ruta
     * @return ResponseEntity con ErrorResponseDTO y status 404 NOT FOUND
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFound(
            ResourceNotFoundException ex, 
            HttpServletRequest request) {
        
        log.error("Recurso no encontrado: {}", ex.getMessage());
        
        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Not Found")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        
        // Agregar detalles estructurados si están disponibles
        if (ex.getResourceName() != null) {
            error.addDetail("resourceName", ex.getResourceName());
            error.addDetail("fieldName", ex.getFieldName());
            error.addDetail("fieldValue", String.valueOf(ex.getFieldValue()));
        }
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Maneja errores de validación de Bean Validation (@Valid).
     * Captura violaciones de @NotNull, @NotBlank, @Size, etc.
     * 
     * @param ex MethodArgumentNotValidException con los errores de validación
     * @param request HttpServletRequest para obtener la ruta
     * @return ResponseEntity con ErrorResponseDTO y status 400 BAD REQUEST
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationErrors(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        
        log.warn("Errores de validación detectados: {} campos inválidos", 
                ex.getBindingResult().getErrorCount());
        
        // Recopilar todos los errores de validación por campo
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });
        
        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Failed")
                .message("Los datos proporcionados no son válidos")
                .path(request.getRequestURI())
                .build();
        
        // Agregar cada error de campo como detalle
        fieldErrors.forEach(error::addDetail);
        
        return ResponseEntity.badRequest().body(error);
    }

    /**
     * Maneja excepciones de lógica de negocio.
     * Se lanza cuando se violan reglas de negocio específicas de la aplicación.
     * 
     * @param ex BusinessException lanzada
     * @param request HttpServletRequest para obtener la ruta
     * @return ResponseEntity con ErrorResponseDTO y status 422 UNPROCESSABLE ENTITY
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseDTO> handleBusinessException(
            BusinessException ex,
            HttpServletRequest request) {
        
        log.error("Error de lógica de negocio: {}", ex.getMessage());
        
        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .error("Business Rule Violation")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        
        // Agregar código de error si está disponible
        if (ex.getErrorCode() != null) {
            error.addDetail("errorCode", ex.getErrorCode());
        }
        
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
    }

    /**
     * Maneja excepciones de validación personalizada.
     * Útil para validaciones complejas que van más allá de Bean Validation.
     * 
     * @param ex ValidationException lanzada
     * @param request HttpServletRequest para obtener la ruta
     * @return ResponseEntity con ErrorResponseDTO y status 400 BAD REQUEST
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationException(
            ValidationException ex,
            HttpServletRequest request) {
        
        log.warn("Error de validación personalizada: {}", ex.getMessage());
        
        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Error")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        
        // Agregar errores de campo si están disponibles
        if (ex.hasErrors()) {
            ex.getErrors().forEach(error::addDetail);
        }
        
        return ResponseEntity.badRequest().body(error);
    }

    /**
     * Maneja cualquier otra excepción no capturada específicamente.
     * Actúa como un catch-all para errores inesperados.
     * 
     * @param ex Exception genérica
     * @param request HttpServletRequest para obtener la ruta
     * @return ResponseEntity con ErrorResponseDTO y status 500 INTERNAL SERVER ERROR
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(
            Exception ex,
            HttpServletRequest request) {
        
        log.error("Error interno del servidor: {}", ex.getMessage(), ex);
        
        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message("Ha ocurrido un error inesperado. Por favor, contacte al administrador.")
                .path(request.getRequestURI())
                .build();
        
        // En desarrollo, agregar detalles técnicos (comentar en producción)
        error.addDetail("exceptionType", ex.getClass().getSimpleName());
        error.addDetail("exceptionMessage", ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    /**
     * Maneja excepciones de IllegalArgumentException.
     * Comúnmente lanzadas por validaciones de parámetros.
     * 
     * @param ex IllegalArgumentException lanzada
     * @param request HttpServletRequest para obtener la ruta
     * @return ResponseEntity con ErrorResponseDTO y status 400 BAD REQUEST
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalArgument(
            IllegalArgumentException ex,
            HttpServletRequest request) {
        
        log.warn("Argumento inválido: {}", ex.getMessage());
        
        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Invalid Argument")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        
        return ResponseEntity.badRequest().body(error);
    }

    /**
     * Maneja excepciones de IllegalStateException.
     * Lanzadas cuando una operación se intenta en un estado inválido.
     * 
     * @param ex IllegalStateException lanzada
     * @param request HttpServletRequest para obtener la ruta
     * @return ResponseEntity con ErrorResponseDTO y status 409 CONFLICT
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalState(
            IllegalStateException ex,
            HttpServletRequest request) {
        
        log.error("Estado inválido: {}", ex.getMessage());
        
        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error("Conflict")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
}
