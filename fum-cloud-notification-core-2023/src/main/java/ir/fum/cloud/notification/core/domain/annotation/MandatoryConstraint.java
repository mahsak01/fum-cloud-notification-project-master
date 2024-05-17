package ir.fum.cloud.notification.core.domain.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created By F.Khojasteh on 6/15/2022
 */

@Documented
@Constraint(validatedBy = MandatoryValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MandatoryConstraint {
    String message() default "فیلد اجباری.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
