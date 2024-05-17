package ir.fum.cloud.notification.core.domain.annotation;

import ir.fum.cloud.notification.core.exception.NotificationExceptionStatus;
import ir.fum.cloud.notification.core.exception.NotificationValidationException;
import ir.fum.cloud.notification.core.util.GeneralUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Created By F.Khojasteh on 6/15/2022
 */

public class MandatoryValidator implements ConstraintValidator<MandatoryConstraint, Object> {
    @Override
    public void initialize(MandatoryConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(Object field, ConstraintValidatorContext constraintValidatorContext) {
        if ((field instanceof ArrayList && GeneralUtils.isNullOrEmpty((List<? extends Object>) field)) || field == null) {
            throw new NotificationValidationException(NotificationExceptionStatus.MISSING_INPUT);
        }

        return true;
    }
}
