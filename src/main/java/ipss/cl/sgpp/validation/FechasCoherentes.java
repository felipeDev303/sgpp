package ipss.cl.sgpp.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * Anotación para validar que las fechas de inicio y término sean coherentes.
 * La fecha de término debe ser posterior a la fecha de inicio.
 */
@Documented
@Constraint(validatedBy = FechasCoherentesValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface FechasCoherentes {
    
    String message() default "La fecha de término debe ser posterior a la fecha de inicio";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
    
    /**
     * Nombre del campo que contiene la fecha de inicio.
     */
    String fechaInicio() default "fechaInicio";
    
    /**
     * Nombre del campo que contiene la fecha de término.
     */
    String fechaTermino() default "fechaTermino";
}
