package ir.fum.cloud.notification.core.domain.annotation.response.error;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * NotAcceptable response.
 * <p>
 * Annotating a service with this annotation will add an
 * ApiResponse to swagger spec with code of {@link this#CODE}
 */

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotAcceptable {

    String CODE = "406";

    @AliasFor("description")
    String value() default "درخواست غیر قابل قبول!";

    @AliasFor("value")
    String description() default "درخواست غیر قابل قبول!";

}