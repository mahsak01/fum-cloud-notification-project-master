package ir.fum.cloud.notification.core.api.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import ir.fum.cloud.notification.core.domain.annotation.response.error.*;
import ir.fum.cloud.notification.core.domain.model.helper.Endpoint;
import ir.fum.cloud.notification.core.domain.model.helper.GenericResponse;
import ir.fum.cloud.notification.core.domain.model.request.SendMailRequest;
import ir.fum.cloud.notification.core.domain.model.srv.SendMailSrv;
import ir.fum.cloud.notification.core.domain.model.vo.UserVO;
import ir.fum.cloud.notification.core.domain.service.MailResendService;
import ir.fum.cloud.notification.core.domain.service.MailSenderService;
import ir.fum.cloud.notification.core.exception.NotificationException;
import ir.fum.cloud.notification.core.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(Endpoint.MAIL)
@Tag(name = "Mail", description = "Mail APIs")
public class MailController {
    private final MailSenderService mailSenderService;
    private final MailResendService mailResendService;

    @PostMapping
    @Operation(summary = "ارسال ایمیل")
    @InvalidRequest
    @Unauthorized
    @AccessDenied
    @Conflict
    @InternalServerError
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
            description = "اطلاعات ارسال ایمیل",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    public GenericResponse<SendMailSrv> sendMail(@RequestBody SendMailRequest sendMailRequest) throws NotificationException {
        UserVO user = AuthUtils.getCurrentUser();
        return mailSenderService.sendMail(user, sendMailRequest, false);
    }

    @PostMapping(Endpoint.BULK)
    @Operation(summary = "ارسال ایمیل به صورت بالک")
    @InvalidRequest
    @Unauthorized
    @AccessDenied
    @Conflict
    @InternalServerError
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
            description = "اطلاعات ارسال ایمیل",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    public GenericResponse<SendMailSrv> sendBulkMail(@RequestBody SendMailRequest sendMailRequest) throws NotificationException {
        UserVO user = AuthUtils.getCurrentUser();
        return mailSenderService.sendMail(user, sendMailRequest, true);
    }

    @PostMapping(Endpoint.RESEND)
    @Operation(summary = "ارسال مجدد ایمیل های خطا خورده")
    @InvalidRequest
    @Unauthorized
    @AccessDenied
    @Conflict
    @InternalServerError
    public GenericResponse<String> resend() throws NotificationException {
        UserVO user = AuthUtils.getCurrentUser();
        return mailResendService.resend(user);
    }

}
