package ir.fum.cloud.notification.core.domain.model.srv;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class SendMailSrv implements Serializable {
    private String requestId;
    private String description;
    private List<String> messageIds;
}
