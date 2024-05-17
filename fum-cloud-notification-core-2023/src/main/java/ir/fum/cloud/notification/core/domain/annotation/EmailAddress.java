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
 * @author Ali Mojahed on 3/27/2023
 * @project notise
 **/

@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = EmailAddressValidator.class)
@Documented
public @interface EmailAddress {

    String message() default "فرمت آدرس ایمیل صحیح نیست.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean canBeNullOrEmpty() default false;
}
