package ir.fum.cloud.notification.core.domain.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import ir.fum.cloud.notification.core.domain.annotation.EmailAddress;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Ali Mojahed on 3/26/2023
 * @project notise
 **/

@Getter
@Setter
@NoArgsConstructor
public class MailConfigUpdateRequest implements Serializable {
    @Schema(description = "آدرس هاست smtp")
    private String smtpHostAddress;

    @Schema(description = "پورت smtp")
    private Integer smtpPort;

    @Schema(description = "آدرس ایمیل")
    @EmailAddress(canBeNullOrEmpty = true)
    private String mailAddress;

    @Schema(description = "رمزعبور قدیمی ایمیل")
    private String oldPassword;

    @Schema(description = "رمزعبور جدید ایمیل")
    private String newPassword;

}
