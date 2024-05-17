package ir.fum.cloud.notification.core.domain.annotation.response.error;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * Conflict response.
 * <p>
 * Annotating a service with this annotation will add an
 * ApiResponse to swagger spec with code of {@link this#CODE}
 */

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Conflict {

    String CODE = "409";

    @AliasFor("description")
    String value() default "تکراری!";

    @AliasFor("value")
    String description() default "تکراری!";

}