package ir.fum.cloud.notification.core.api.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import ir.fum.cloud.notification.core.domain.annotation.response.error.*;
import ir.fum.cloud.notification.core.domain.model.helper.Endpoint;
import ir.fum.cloud.notification.core.domain.model.helper.GenericResponse;
import ir.fum.cloud.notification.core.domain.model.request.MailConfigCreateRequest;
import ir.fum.cloud.notification.core.domain.model.request.MailConfigUpdateRequest;
import ir.fum.cloud.notification.core.domain.model.srv.MailConfigSrv;
import ir.fum.cloud.notification.core.domain.model.vo.UserVO;
import ir.fum.cloud.notification.core.domain.service.MailConfigService;
import ir.fum.cloud.notification.core.exception.NotificationException;
import ir.fum.cloud.notification.core.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Ali Mojahed on 3/26/2023
 * @project notise
 **/

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(Endpoint.MAIL + Endpoint.CONFIGS)
@Tag(name = "MailConfig", description = "Mail Config APIs")
public class MailConfigController {
    private final MailConfigService mailConfigService;


    @PostMapping
    @Operation(summary = "ایجاد تنظیمات ارسال ایمیل")
    @InvalidRequest
    @Unauthorized
    @AccessDenied
    @Conflict
    @InternalServerError
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
            description = "اطلاعات ایجاد تنظیمات ارسال ایمیل",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    public GenericResponse<Long> createMailConfig(@RequestBody MailConfigCreateRequest mailConfigCreateRequest) throws NotificationException {
        UserVO user = AuthUtils.getCurrentUser();
        return mailConfigService.addMailConfig(user, mailConfigCreateRequest);
    }

    @PatchMapping(Endpoint.ID_PATH_PARAM)
    @Operation(summary = "به روزرسانی تنظیمات ارسال ایمیل")
    @InvalidRequest
    @Unauthorized
    @AccessDenied
    @NotFound
    @Conflict
    @InternalServerError
    @Parameter(name = "id", description = "شناسه تنظیمات ارسال ایمیل", example = "123", required = true)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
            description = "اطلاعات به روزرسانی تنظیمات ارسال ایمیل",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    public GenericResponse<Long> updateMailConfig(
            @PathVariable("id") long id,
            @RequestBody MailConfigUpdateRequest mailConfigUpdateRequest
    ) throws NotificationException {
        UserVO user = AuthUtils.getCurrentUser();
        return mailConfigService.updateMailConfig(user, id, mailConfigUpdateRequest);
    }


    @GetMapping(Endpoint.ID_PATH_PARAM)
    @Operation(summary = "دریافت تنظیمات ارسال ایمیل با شناسه تنظیمات")
    @InvalidRequest
    @Unauthorized
    @AccessDenied
    @NotFound
    @InternalServerError
    @Parameter(name = "id", description = "شناسه تنظیمات ارسال ایمیل", example = "123", required = true)
    public GenericResponse<MailConfigSrv> getMailConfig(@PathVariable("id") long id) throws NotificationException {
        UserVO user = AuthUtils.getCurrentUser();
        return mailConfigService.getMailConfig(user, id);
    }


    @GetMapping()
    @Operation(summary = "دریافت تنظیمات ارسال ایمیل")
    @InvalidRequest
    @Unauthorized
    @AccessDenied
    @InternalServerError
    @Parameter(name = "offset", description = "شمماره اولین آیتم (پیشفرض: 0)", example = "0")
    @Parameter(name = "size", description = "تعداد آیتم بازگشتی (پیشفرض: 50)", example = "10")
    public GenericResponse<List<MailConfigSrv>> getMailConfigs(
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(value = "size", required = false, defaultValue = "50") int size
    ) throws NotificationException {

        UserVO user = AuthUtils.getCurrentUser();
        return mailConfigService.getMailConfigs(user, size, offset);
    }


    @DeleteMapping(Endpoint.ID_PATH_PARAM)
    @Operation(summary = "حذف تنظیمات ارسال ایمیل")
    @InvalidRequest
    @Unauthorized
    @AccessDenied
    @NotFound
    @InternalServerError
    @Parameter(name = "id", description = "شناسه تنظیمات ارسال ایمیل", example = "123", required = true)
    public GenericResponse<Long> deleteMailConfig(@PathVariable("id") long id) throws NotificationException {
        UserVO user = AuthUtils.getCurrentUser();
        return mailConfigService.deleteMailConfig(user, id);
    }

}
