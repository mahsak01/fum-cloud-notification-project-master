package ir.fum.cloud.notification.core.domain.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Ali Mojahed on 11/22/2022
 * @project notise
 **/

@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = NumericStringValidator.class)
@Documented
public @interface NumericString {
    String message() default "مقدار این رشته باید تمام عددی باشد.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean canBeNullOrEmpty() default false;
}
