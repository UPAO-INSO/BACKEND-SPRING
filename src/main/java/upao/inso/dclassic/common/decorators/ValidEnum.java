package upao.inso.dclassic.common.decorators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import upao.inso.dclassic.common.validation.EnumConstraintValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EnumConstraintValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEnum {
    String message() default "Invalid Enum, must be one of: {values}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    Class<? extends Enum<?>> enumClass();
}
