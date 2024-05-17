package ir.fum.cloud.notification.core.domain.annotation.response.error;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * InvalidRequest response.
 * <p>
 * Annotating a service with this annotation will add an
 * ApiResponse to swagger spec with code of {@link this#CODE}
 */

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface InvalidRequest {

    String CODE = "400";

    @AliasFor("description")
    String value() default "درخواست نامعتبر!";

    @AliasFor("value")
    String description() default "درخواست نامعتبر!";

}