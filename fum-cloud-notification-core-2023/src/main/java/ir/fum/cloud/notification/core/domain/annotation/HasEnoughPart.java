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
 * @author Ali Mojahed on 12/4/2022
 * @project notise
 **/

@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = HasEnoughPartValidator.class)
@Documented
public @interface HasEnoughPart {

    String message() default "مقادیر وارد شده تعداد پارت های مجاز را ندارند";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int part() default 2;

    String delimiter() default "\\.";

}
