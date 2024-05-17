package ir.fum.cloud.notification.core.domain.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MailQueueVO implements Serializable {
    private SendMailVO sendMailVO;
    private long configId;
    private String requestId;
}
