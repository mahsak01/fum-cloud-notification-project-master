package ir.fum.cloud.notification.core.domain.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import ir.fum.cloud.notification.core.domain.annotation.EmailAddress;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Ali Mojahed on 3/26/2023
 * @project notise
 **/

@Getter
@Setter
@NoArgsConstructor
public class MailConfigCreateRequest implements Serializable {

    @NotBlank(message = "نام سرویس را وارد کنید.")
    @Schema(description = "نام سرویس")
    private String serviceName;

    @NotBlank(message = "آدرس هاست smtp را وارد کنید.")
    @Schema(description = "آدرس هاست smtp")
    private String smtpHostAddress;

    @NotNull(message = "پورت smtp را وارد کنید.")
    @Schema(description = "پورت smtp")
    private Integer smtpPort;

    @NotBlank(message = "آدرس ایمیل را وارد کنید.")
    @EmailAddress
    @Schema(description = "آدرس ایمیل")
    private String mailAddress;

    @NotBlank(message = "رمزعبور ایمیل را وارد کنید.")
    @Schema(description = "رمزعبور ایمیل")
    private String mailPassword;

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress.toLowerCase();
    }
}
