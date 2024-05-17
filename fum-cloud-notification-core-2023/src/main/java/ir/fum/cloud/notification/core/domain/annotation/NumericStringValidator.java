package ir.fum.cloud.notification.core.domain.annotation;

import ir.fum.cloud.notification.core.util.GeneralUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Ali Mojahed on 11/22/2022
 * @project notise
 **/
public class NumericStringValidator implements ConstraintValidator<NumericString, String> {

    private boolean canBeNullOrEmpty;

    @Override
    public void initialize(NumericString constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);

        canBeNullOrEmpty = constraintAnnotation.canBeNullOrEmpty();

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return (canBeNullOrEmpty || !GeneralUtils.isNullOrEmpty(s)) && GeneralUtils.isNumeric(s);
    }
}
