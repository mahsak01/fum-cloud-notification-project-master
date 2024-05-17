package ir.fum.cloud.notification.core.domain.annotation.response.success;


import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * Created response.
 * <p>
 * Annotating a service with this annotation will add an
 * ApiResponse to swagger spec with code of
 * {@link this#CODE} and type of {@link this#type()} or {@link this#$ref()} if specified.
 */

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Created {

    String CODE = "201";

    @AliasFor("description")
    String value() default "ایجاد شد!";

    @AliasFor("value")
    String description() default "ایجاد شد!";

    Class<?> type() default Void.class;

    Class<?> elementTypes() default Void.class;

    String $ref() default "";

}