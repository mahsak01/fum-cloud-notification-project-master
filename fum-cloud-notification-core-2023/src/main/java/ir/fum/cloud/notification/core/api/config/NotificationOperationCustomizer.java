package ir.fum.cloud.notification.core.api.config;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import ir.fum.cloud.notification.core.domain.annotation.response.error.*;
import ir.fum.cloud.notification.core.domain.annotation.response.success.Created;
import ir.fum.cloud.notification.core.domain.annotation.response.success.OK;
import ir.fum.cloud.notification.core.domain.model.helper.GenericResponse;
import lombok.SneakyThrows;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Operation Customizer.
 * Currently, it just affects the custom response annotations.
 */

@Component
public class NotificationOperationCustomizer implements OperationCustomizer {


    @Override
    public Operation customize(Operation operation,
                               HandlerMethod handlerMethod) {

        applySuccessAnnotation(operation, handlerMethod, Created.class, true);
        applySuccessAnnotation(operation, handlerMethod, OK.class, false);
        applyErrorAnnotations(operation, handlerMethod);


        return operation;
    }

    @SneakyThrows
    private <A extends Annotation> void applySuccessAnnotation(Operation operation,
                                                               HandlerMethod handlerMethod,
                                                               Class<A> classOfA,
                                                               boolean replace) {

        A annotation = handlerMethod.getMethodAnnotation(classOfA);

        if (annotation != null) {

            Content content;
            Type type = (Type) classOfA.getMethod("type").invoke(annotation);
            String ref = (String) classOfA.getMethod("$ref").invoke(annotation);
            String description = (String) classOfA.getMethod("description").invoke(annotation);
            String code = (String) classOfA.getField("CODE").get(null);

            if (!ref.isEmpty()) {

                content = new Content()
                        .addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                                new MediaType().schema(new Schema<>().$ref(ref)));

            } else {

                ResolvedSchema resolvedSchema = ModelConverters.getInstance()
                        .resolveAsResolvedSchema(new AnnotatedType(type));

                content = new Content()
                        .addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                                new MediaType().schema(resolvedSchema.schema));

            }

            if (replace)
                operation.responses(new ApiResponses());

            operation.getResponses().addApiResponse(code, new ApiResponse()
                    .description(description)
                    .content(content));
        }
    }

    private void applyErrorAnnotations(Operation operation,
                                       HandlerMethod handlerMethod) {

        ResolvedSchema apiErrorVOSchema = ModelConverters.getInstance()
                .resolveAsResolvedSchema(new AnnotatedType(GenericResponse.class));

        Content content = new Content()
                .addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                        new MediaType().schema(apiErrorVOSchema.schema));

        applyErrorAnnotation(operation, handlerMethod, content, InvalidRequest.class);
        applyErrorAnnotation(operation, handlerMethod, content, Unauthorized.class);
        applyErrorAnnotation(operation, handlerMethod, content, AccessDenied.class);
        applyErrorAnnotation(operation, handlerMethod, content, NotFound.class);
        applyErrorAnnotation(operation, handlerMethod, content, NotAcceptable.class);
        applyErrorAnnotation(operation, handlerMethod, content, Unprocessable.class);
        applyErrorAnnotation(operation, handlerMethod, content, Conflict.class);
        applyErrorAnnotation(operation, handlerMethod, content, InternalServerError.class);
    }

    @SneakyThrows
    private <A extends Annotation> void applyErrorAnnotation(Operation operation,
                                                             HandlerMethod handlerMethod,
                                                             Content content,
                                                             Class<A> classOfA) {

        A annotation = handlerMethod.getMethodAnnotation(classOfA);

        if (annotation != null) {
            operation.getResponses().addApiResponse((String) classOfA.getField("CODE").get(null),
                    new ApiResponse()
                            .description((String) classOfA.getMethod("description").invoke(annotation))
            );
        }
    }

}
