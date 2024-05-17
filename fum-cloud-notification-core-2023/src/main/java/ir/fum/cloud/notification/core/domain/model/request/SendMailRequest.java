package ir.fum.cloud.notification.core.domain.model.request;

import ir.fum.cloud.notification.core.domain.annotation.EmailAddress;
import ir.fum.cloud.notification.core.domain.annotation.EmailAddresses;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;


@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class SendMailRequest implements Serializable {
    @NotBlank(message = "نام سرویس را وارد کنید.")
    private String serviceName;

    @NotBlank(message = "محتوای ایمیل را وارد کنید.")
    private String content;

    private String plainText;

    @NotBlank(message = "موضوع ایمیل را وارد کنید.")
    private String subject;

    @EmailAddress(canBeNullOrEmpty = true, message = "آدرس پاسخ صحیح نیست.")
    private String replayAddress;


    @EmailAddresses(canBeNullOrEmpty = true, message = "آدرس وارد شده اشتباه است.")
    private List<String> cc;

    @EmailAddresses(canBeNullOrEmpty = true, message = "آدرس وارد شده اشتباه است.")
    private List<String> bcc;

    private List<String> fileHashes;

    @Size(min = 1, message = "دریافت کنندگان را وارد کنید.")
    @EmailAddresses
    private List<String> to;


    private boolean resend;

}
