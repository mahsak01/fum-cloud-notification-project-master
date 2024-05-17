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
 * @author Ali Mojahed on 12/19/2022
 * @project notise
 **/
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = PhoneNumberValidator.class)
@Documented
public @interface PhoneNumber {
    String message() default "فرمت تلفن همراه صحیح نیست.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean canBeNullOrEmpty() default false;
}
