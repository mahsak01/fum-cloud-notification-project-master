package ir.fum.cloud.notification.core.exception;

import lombok.Getter;

import javax.validation.ConstraintDeclarationException;

@Getter
public class NotificationValidationException extends ConstraintDeclarationException {

    private int code;
    private String developerMessage;
    private int status;

    public NotificationValidationException(NotificationExceptionStatus e, String developerMessage) {
        this.code = e.getCode();
        this.developerMessage = developerMessage;
        this.status = e.getStatus();
    }

    public NotificationValidationException(NotificationExceptionStatus e) {
        this.code = e.getCode();
        this.developerMessage = e.getDescription();
        this.status = e.getStatus();
    }

}
