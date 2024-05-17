package ir.fum.cloud.notification.core.domain.annotation;

import ir.fum.cloud.notification.core.util.GeneralUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class EmailAddressesValidator implements ConstraintValidator<EmailAddress, List<String>> {
    private boolean canBeNullOrEmpty;

    @Override
    public void initialize(EmailAddress constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);

        canBeNullOrEmpty = constraintAnnotation.canBeNullOrEmpty();
    }

    @Override
    public boolean isValid(List<String> strings, ConstraintValidatorContext constraintValidatorContext) {
        if (canBeNullOrEmpty && GeneralUtils.isNullOrEmpty(strings)) {
            return true;
        }

        boolean isValid = false;

        if (!GeneralUtils.isNullOrEmpty(strings)) {
            isValid = strings.stream()
                    .allMatch(GeneralUtils::emailIsValid);

        }

        return isValid;

    }
}
