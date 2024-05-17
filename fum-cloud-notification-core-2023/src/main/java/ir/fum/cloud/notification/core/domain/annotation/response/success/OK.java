package ir.fum.cloud.notification.core.domain.annotation.response.success;


import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * OK response.
 * <p>
 * Annotating a service with this annotation will add an
 * ApiResponse to swagger spec with code of
 * {@link this#CODE} and type of {@link this#type()} or {@link this#$ref()} if specified.
 */

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OK {

    String CODE = "200";

    @AliasFor("description")
    String value() default "OK!";

    @AliasFor("value")
    String description() default "OK!";

    Class<?> type() default Void.class;

    Class<?> elementTypes() default Void.class;

    String $ref() default "";

}