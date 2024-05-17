package ir.fum.cloud.notification.core.domain.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NullListItemValidator.class)
public @interface RemoveNullItems {

    String message() default "Null values are not allowed in array fields.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
