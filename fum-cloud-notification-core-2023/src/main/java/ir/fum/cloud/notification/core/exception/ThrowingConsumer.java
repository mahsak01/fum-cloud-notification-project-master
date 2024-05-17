package ir.fum.cloud.notification.core.exception;

@FunctionalInterface
public interface ThrowingConsumer<T, E extends NotificationException> {

    void accept(T t) throws E;

}