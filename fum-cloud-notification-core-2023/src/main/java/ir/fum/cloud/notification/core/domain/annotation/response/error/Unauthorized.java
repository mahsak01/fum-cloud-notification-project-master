package ir.fum.cloud.notification.core.domain.annotation.response.error;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * Unauthorized response.
 * <p>
 * Annotating a service with this annotation will add an
 * ApiResponse to swagger spec with code of {@link this#CODE}
 */

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Unauthorized {

    String CODE = "401";

    @AliasFor("description")
    String value() default "عدم احراز هویت!";

    @AliasFor("value")
    String description() default "عدم احراز هویت!";

}