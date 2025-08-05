package upao.inso.dclassic.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.SneakyThrows;
import org.passay.*;
import upao.inso.dclassic.common.decorators.ValidPassword;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public void initialize(final ValidPassword arg0) {
    }

    @SneakyThrows
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        Properties props = new Properties();

        InputStream input = getClass()
                .getClassLoader().getResourceAsStream("passay.properties");
        props.load(input);

        PasswordValidator validator = getPasswordValidator(props);
        RuleResult result = validator.validate(new PasswordData(password));

        if (result.isValid()) return true;

        List<String> messages = validator.getMessages(result);

        messages.forEach(msg -> {
            context.buildConstraintViolationWithTemplate(msg)
                    .addConstraintViolation();
        });
        context.disableDefaultConstraintViolation();
        return false;
    }

    private static PasswordValidator getPasswordValidator(Properties props) {
        MessageResolver resolver = new PropertiesMessageResolver(props);

        return new PasswordValidator(resolver, Arrays.asList(
                new LengthRule(8, 20),
                new CharacterRule(EnglishCharacterData.LowerCase, 1),
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                new CharacterRule(EnglishCharacterData.Digit, 1),
                new CharacterRule(EnglishCharacterData.Special, 1),
                new WhitespaceRule()
        ));
    }
}
