package ir.fum.cloud.notification.core.domain.model.vo;

import ir.fum.cloud.notification.core.data.hibernate.entity.MailConfig;
import ir.fum.cloud.notification.core.domain.model.request.SendMailRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class SendMailVO implements Serializable {
    private long user;
    private MailConfig mailConfig;
    private String requestId;
    private SendMailRequest sendMailRequest;
    private List<String> receivers;
    private List<String> fileNames;
    private List<String> messageIds;
    private Map<String, SendMailRequestMetadata> requestInformation;
    private boolean throwException;

    private boolean resend;
    private int resendCounter = 0;

    public void resendTry() {
        resendCounter += 1;
    }

}
