package ir.fum.cloud.notification.core.domain.model.vo;

import ir.fum.cloud.notification.core.data.hibernate.entity.model.RequestState;
import ir.fum.cloud.notification.core.util.GeneralUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class SendMailRequestMetadata implements Serializable {
    private String receiver;
    private Map<String, Object> metadata;
    private RequestState state;

    public SendMailRequestMetadata(String receiver, String messageId) {
        this(messageId);
        this.receiver = receiver;
    }

    public SendMailRequestMetadata(String messageId) {
        metadata = new HashMap<>();
        metadata.put("messageId", messageId);
    }


    public void addToMetadata(String key, Object value) {
        if (GeneralUtils.isNullOrEmpty(metadata)) {
            metadata = new HashMap<>();
        }

        metadata.put(key, value);

    }

}
