package ir.fum.cloud.notification.core.data.hibernate.entity.model;


import lombok.Getter;

@Getter
public enum RequestState {
    SENT(0),
    NOT_SENT(1),
    SCHEDULED(2),
    QUEUED(3),
    EXPIRED(4),
    PROVIDER_ERROR(5),
    INVALID_DATA(6),
    DATA_ERROR(7),
    INTERNAL_ERROR(8),
    RESEND_QUEUE(9),
    EXCEED_LIMITATION(10);

    private int type;

    RequestState(int type) {
        this.type = type;
    }

}
