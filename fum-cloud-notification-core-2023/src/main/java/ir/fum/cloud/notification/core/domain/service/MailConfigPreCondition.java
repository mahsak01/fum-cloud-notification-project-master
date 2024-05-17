package ir.fum.cloud.notification.core.domain.service;

import ir.fum.cloud.notification.core.data.hibernate.entity.MailConfig;
import ir.fum.cloud.notification.core.domain.model.request.MailConfigUpdateRequest;
import ir.fum.cloud.notification.core.exception.NotificationException;
import ir.fum.cloud.notification.core.exception.NotificationExceptionStatus;
import ir.fum.cloud.notification.core.util.AdvancedEncryptionStandard;
import ir.fum.cloud.notification.core.util.GeneralUtils;
import org.springframework.stereotype.Component;

/**
 * @author Ali Mojahed on 3/26/2023
 * @project notise
 **/

@Component
public class MailConfigPreCondition {
    public void checkUpdateMailConfigArguments(MailConfigUpdateRequest mailConfigUpdateRequest, MailConfig mailConfig) throws NotificationException {
        String oldPassword = mailConfigUpdateRequest.getOldPassword();
        String newPassword = mailConfigUpdateRequest.getNewPassword();

        if ((!GeneralUtils.isNullOrEmpty(oldPassword) && GeneralUtils.isNullOrEmpty(newPassword))) {
            throw NotificationException.exception(
                    NotificationExceptionStatus.INVALID_INPUT,
                    "رمزعبور جدید وارد کنید."
            );
        }


        if (GeneralUtils.isNullOrEmpty(oldPassword) && !GeneralUtils.isNullOrEmpty(newPassword)) {
            throw NotificationException.exception(
                    NotificationExceptionStatus.INVALID_INPUT,
                    "رمزعبور قدیمی وارد کنید."
            );
        }


        if (!GeneralUtils.isNullOrEmpty(oldPassword) && !GeneralUtils.isNullOrEmpty(newPassword)) {

            if (!AdvancedEncryptionStandard.decrypt(mailConfig.getPassword()).equals(oldPassword)) {
                throw NotificationException.exception(
                        NotificationExceptionStatus.INVALID_INPUT,
                        "رمز عبور قبلی اشتباه است."
                );
            }

            if (oldPassword.equals(newPassword)) {
                throw NotificationException.exception(
                        NotificationExceptionStatus.INVALID_INPUT,
                        "رمز عبور قبلی و رمز فعلی یکسان است."
                );
            }

        }


    }
}
