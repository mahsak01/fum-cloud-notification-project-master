package ir.fum.cloud.notification.core.domain.annotation.response.success;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * Accepted response.
 * <p>
 * Annotating a service with this annotation will add an
 * ApiResponse to swagger spec with code of
 * {@link this#CODE} and type of {@link this#type()} or {@link this#$ref()} if specified.
 */

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Accepted {

    String CODE = "202";

    @AliasFor("description")
    String value() default "درخواست پذیرفته شد!";

    @AliasFor("value")
    String description() default "درخواست پذیرفته شد!";

    Class<?> type() default Void.class;

    Class<?> elementTypes() default Void.class;

    String $ref() default "";

}