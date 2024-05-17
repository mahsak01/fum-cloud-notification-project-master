package ir.fum.cloud.notification.core.domain.annotation.response.error;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * NotFound response.
 * <p>
 * Annotating a service with this annotation will add an
 * ApiResponse to swagger spec with code of {@link this#CODE}
 */

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotFound {

    String CODE = "404";

    @AliasFor("description")
    String value() default "پیدا نشد!";

    @AliasFor("value")
    String description() default "پیدا نشد!";

}