package ir.fum.cloud.notification.core.domain.annotation;

import ir.fum.cloud.notification.core.util.GeneralUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Ali Mojahed on 12/19/2022
 * @project notise
 **/
public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {
    private boolean canBeNullOrEmpty;

    @Override
    public void initialize(PhoneNumber constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);

        canBeNullOrEmpty = constraintAnnotation.canBeNullOrEmpty();

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return (canBeNullOrEmpty && GeneralUtils.isNullOrEmpty(s)) || (!GeneralUtils.isNullOrEmpty(s) && GeneralUtils.phoneIsValid(s));
    }
}
