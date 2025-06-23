package upao.inso.dclassic.common.decorators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import upao.inso.dclassic.common.passay.PasswordConstraintValidator;

import java.lang.annotation.*;


@Documented
@Constraint(
        validatedBy = PasswordConstraintValidator.class
)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
    String message() default "Invalid Password";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
