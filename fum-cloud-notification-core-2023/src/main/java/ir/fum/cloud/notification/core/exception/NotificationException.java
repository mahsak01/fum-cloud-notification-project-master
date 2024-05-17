package ir.fum.cloud.notification.core.exception;

import ir.fum.cloud.notification.core.util.mapper.GsonUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationException extends Exception {

    private int code;
    private String developerMessage;
    private int status;
    private RollbackEngine<?, ?> rollbackEngine;

    public static NotificationException exception(NotificationExceptionStatus status, String message) {
        NotificationException exception = new NotificationException();

        exception.setCode(status.getCode());
        exception.setDeveloperMessage(message);
        exception.setStatus(status.getStatus());

        return exception;
    }

    public static NotificationException exception(NotificationException e,
                                                  RollbackEngine<?, ?> rollbackEngine) {
        NotificationException exception = new NotificationException();


        exception.setDeveloperMessage(e.getDeveloperMessage());
        exception.setStatus(e.getStatus());
        exception.setCode(e.getCode());
        exception.setRollbackEngine(rollbackEngine);

        return exception;
    }

    public static NotificationException exception(NotificationException e) {
        NotificationException exception = new NotificationException();

        exception.setDeveloperMessage(e.getDeveloperMessage());
        exception.setStatus(e.getStatus());
        exception.setCode(e.getCode());

        return exception;
    }

    public static NotificationException exception(int httpStatus) {
        NotificationException notificationException = new NotificationException();
        notificationException.setCode(httpStatus);
        return notificationException;
    }

    public static NotificationException exception(NotificationExceptionStatus exceptionStatus) {
        NotificationException exception = new NotificationException();

        exception.setDeveloperMessage(exceptionStatus.description);
        exception.setCode(exceptionStatus.code);
        exception.setStatus(exceptionStatus.status);
        return exception;
    }

    public static NotificationException exception(NotificationExceptionStatus exceptionStatus, int status) {
        NotificationException exception = new NotificationException();

        exception.setDeveloperMessage(exceptionStatus.description);
        exception.setCode(exceptionStatus.code);
        exception.setStatus(status);
        return exception;
    }

    public static NotificationException exception(int code, int status, String message) {
        NotificationException exception = new NotificationException();

        exception.setCode(code);
        exception.setDeveloperMessage(message);
        exception.setStatus(status);

        return exception;
    }

    public String getJson() {
        return GsonUtils.getJson(this);
    }

    public boolean hasRollbackEngine() {
        return rollbackEngine != null;
    }

}
