package ir.fum.cloud.notification.core.domain.annotation;

import ir.fum.cloud.notification.core.util.GeneralUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

/**
 * @author Ali Mojahed on 12/4/2022
 * @project notise
 **/


public class HasEnoughPartValidator implements ConstraintValidator<HasEnoughPart, String> {

    private int part;
    private String delimiter;

    @Override
    public void initialize(HasEnoughPart constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);

        this.part = constraintAnnotation.part();
        this.delimiter = constraintAnnotation.delimiter();

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (GeneralUtils.isNullOrEmpty(s)) {
            return true;
        }

        String[] parts = s.split(delimiter);

        return parts.length >= part && Arrays.stream(parts).noneMatch(p -> p.trim().isEmpty());
    }
}
