package ir.fum.cloud.notification.core.domain.model.srv;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class MailConfigSrv extends BaseSrv {
    private String serviceName;
    private String smtpHostAddress;
    private String smtpPort;
    private String mailAddress;
}
