package ir.fum.cloud.notification.core.domain.annotation.response.error;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * Unprocessable response.
 * <p>
 * Annotating a service with this annotation will add an
 * ApiResponse to swagger spec with code of {@link this#CODE}
 */

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Unprocessable {

    String CODE = "422";

    @AliasFor("description")
    String value() default "غیر قابل پردازش!";

    @AliasFor("value")
    String description() default "غیر قابل پردازش!";

}