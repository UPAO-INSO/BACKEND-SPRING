package upao.inso.dclassic.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import upao.inso.dclassic.common.decorators.ValidEnum;

import java.util.Arrays;
import java.util.stream.Collectors;

public class EnumConstraintValidator implements ConstraintValidator<ValidEnum, Enum<?>> {
    private String values;
    private Class<? extends Enum<?>> enumClass;

    @Override
    public void initialize(final ValidEnum constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumClass();
        values = Arrays.stream(enumClass.getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }

    @Override
    public boolean isValid(Enum<?> value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) return false;
        boolean valid = Arrays.asList(enumClass.getEnumConstants()).contains(value);
        if (!valid) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(
                    "Value must be one of: " + values
            ).addConstraintViolation();
        }
        return valid;
    }
}
