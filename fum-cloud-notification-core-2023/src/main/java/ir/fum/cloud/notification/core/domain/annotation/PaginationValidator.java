package ir.fum.cloud.notification.core.domain.annotation;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PaginationValidator implements ConstraintValidator<Pagination, Integer> {
    @Override
    public boolean isValid(Integer i, ConstraintValidatorContext constraintValidatorContext) {
        return i == null || i >= 0;
    }
}

