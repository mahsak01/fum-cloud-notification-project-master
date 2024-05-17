package ir.fum.cloud.notification.core.domain.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = PaginationValidator.class)
@Documented
public @interface Pagination {
    String message() default "مقادیر وارد شده برای محدود کردن ورودی ها باید نامنفی باشد.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}


