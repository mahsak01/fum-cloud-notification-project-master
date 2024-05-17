package ir.fum.cloud.notification.core.data.hibernate.entity.model;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MessageType {
    SEND_EMAIL_MESSAGE_TYPE(0);

    private final int type;
}
