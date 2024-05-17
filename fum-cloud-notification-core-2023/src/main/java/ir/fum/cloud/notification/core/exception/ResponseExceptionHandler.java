package ir.fum.cloud.notification.core.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;

/**
 * Handle exception types
 */

@Slf4j
@ControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

    public static String getErrorMessageOfArgumentNotValidException(MethodArgumentNotValidException ex) {
        ArrayList<String> allErrors = new ArrayList<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(fieldError -> allErrors.add(fieldError.getField() + ": [" + fieldError.getDefaultMessage() + "]"));

        ex.getBindingResult()
                .getGlobalErrors()
                .forEach(objectError -> allErrors.add(objectError.getObjectName() + ": [" + objectError.getDefaultMessage() + "]"));


        return String.join(" , ", allErrors);

    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(NotificationException.class)
    public ResponseEntity<Object> notificationExceptionHandler(WebRequest request,
                                                               NotificationException exception) {
        logger.error(exception.getDeveloperMessage());
        return handleExceptionInternal(exception,
                NotificationExceptionStatus.setErrorMessage(exception),
                new HttpHeaders(),
                HttpStatus.valueOf(exception.getStatus() != 0 ? exception.getStatus() : 200),
                request);
    }

    @ExceptionHandler(NotificationValidationException.class)
    public ResponseEntity<Object> projectValidationExceptionHandler(WebRequest request,
                                                                    NotificationValidationException exception) {
        logger.error(exception.getDeveloperMessage());
        return handleExceptionInternal(exception,
                NotificationExceptionStatus.setErrorMessage(exception),
                new HttpHeaders(),
                HttpStatus.valueOf(exception.getStatus() != 0 ? exception.getStatus() : 200),
                request);
    }

    /**
     * This method handle validation exceptions in spring validator inputs.
     */
    @NonNull
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatus status,
                                                                  @NonNull WebRequest request) {
        log.error(ex.getMessage());

        String message = getErrorMessageOfArgumentNotValidException(ex);

        return handleExceptionInternal(ex,
                NotificationExceptionStatus.setErrorMessage(message, NotificationExceptionStatus.INVALID_INPUT),
                new HttpHeaders(),
                HttpStatus.valueOf(NotificationExceptionStatus.INVALID_INPUT.getStatus()),
                request
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> constraintValidationHandler(WebRequest request,
                                                              ConstraintViolationException exception) {
        log.error(exception.getMessage());

        String message = (exception.getConstraintViolations() != null &&
                exception.getConstraintViolations().stream().findFirst().isPresent())
                ? exception.getConstraintViolations().stream().findFirst().get().getMessageTemplate()
                : exception.getMessage();

        return handleExceptionInternal(exception,
                NotificationExceptionStatus.setErrorMessage(message, NotificationExceptionStatus.INVALID_INPUT),
                new HttpHeaders(),
                HttpStatus.valueOf(NotificationExceptionStatus.INVALID_INPUT.getStatus()),
                request
        );
    }

    @NonNull
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(@NonNull Exception ex,
                                                             Object body,
                                                             @NonNull HttpHeaders headers,
                                                             @NonNull HttpStatus status,
                                                             @NonNull WebRequest request) {
        logger.error(ex);
        return ResponseEntity
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body);
    }

}
