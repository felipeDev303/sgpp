package ipss.cl.sgpp.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.time.LocalDate;

/**
 * Validador para la anotación @FechasCoherentes.
 * Verifica que la fecha de término sea posterior a la fecha de inicio.
 */
public class FechasCoherentesValidator implements ConstraintValidator<FechasCoherentes, Object> {
    
    private String fechaInicioFieldName;
    private String fechaTerminoFieldName;
    
    @Override
    public void initialize(FechasCoherentes constraintAnnotation) {
        this.fechaInicioFieldName = constraintAnnotation.fechaInicio();
        this.fechaTerminoFieldName = constraintAnnotation.fechaTermino();
    }
    
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // @NotNull se encarga de validar nulos
        }
        
        try {
            LocalDate fechaInicio = getFieldValue(value, fechaInicioFieldName, LocalDate.class);
            LocalDate fechaTermino = getFieldValue(value, fechaTerminoFieldName, LocalDate.class);
            
            // Si alguna fecha es nula, dejamos que @NotNull se encargue
            if (fechaInicio == null || fechaTermino == null) {
                return true;
            }
            
            // Validar que fechaTermino > fechaInicio
            boolean isValid = fechaTermino.isAfter(fechaInicio);
            
            if (!isValid) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                    "La fecha de término (" + fechaTermino + ") debe ser posterior a la fecha de inicio (" + fechaInicio + ")"
                ).addPropertyNode(fechaTerminoFieldName)
                 .addConstraintViolation();
            }
            
            return isValid;
            
        } catch (Exception e) {
            return false;
        }
    }
    
    @SuppressWarnings("unchecked")
    private <T> T getFieldValue(Object object, String fieldName, Class<T> expectedType) throws Exception {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        Object value = field.get(object);
        
        if (value == null) {
            return null;
        }
        
        if (expectedType.isInstance(value)) {
            return (T) value;
        }
        
        throw new IllegalArgumentException(
            "El campo " + fieldName + " no es del tipo esperado " + expectedType.getSimpleName()
        );
    }
}
