package ir.fum.cloud.notification.core.domain.service;

import ir.fum.cloud.notification.core.configuration.MailPropertiesConfiguration;
import ir.fum.cloud.notification.core.data.hibernate.entity.MailConfig;
import ir.fum.cloud.notification.core.data.hibernate.repository.MailConfigRepository;
import ir.fum.cloud.notification.core.domain.annotation.MandatoryConstraint;
import ir.fum.cloud.notification.core.domain.annotation.Pagination;
import ir.fum.cloud.notification.core.domain.model.helper.GenericResponse;
import ir.fum.cloud.notification.core.domain.model.request.MailConfigCreateRequest;
import ir.fum.cloud.notification.core.domain.model.request.MailConfigUpdateRequest;
import ir.fum.cloud.notification.core.domain.model.srv.MailConfigSrv;
import ir.fum.cloud.notification.core.domain.model.vo.UserVO;
import ir.fum.cloud.notification.core.exception.NotificationException;
import ir.fum.cloud.notification.core.exception.NotificationExceptionStatus;
import ir.fum.cloud.notification.core.util.AdvancedEncryptionStandard;
import ir.fum.cloud.notification.core.util.GeneralUtils;
import ir.fum.cloud.notification.core.util.request.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * @author Ali Mojahed on 3/26/2023
 * @project notise
 **/

@Service
@Log4j2
@RequiredArgsConstructor
@Validated
public class MailConfigService {
    private final SessionFactory sessionFactory;
    private final MailConfigRepository mailConfigRepository;

    private final MailConfigPreCondition mailConfigPreCondition;

    private final MailPropertiesConfiguration mailPropertiesConfiguration;


    @Transactional(value = "projectTransactionManager", rollbackFor = Exception.class)
    public GenericResponse<Long> addMailConfig(@NotNull UserVO user,
                                               @Valid @MandatoryConstraint MailConfigCreateRequest mailConfigRequest) throws NotificationException {
        Session session = sessionFactory.getCurrentSession();

        if (mailConfigRepository.getMailConfigByServiceName(
                session,
                mailConfigRequest.getServiceName()).isPresent()) {

            throw NotificationException.exception(
                    NotificationExceptionStatus.ALREADY_EXIST,
                    "تنظیمات ارسال با نام سرویس وارد شده موجود است."
            );
        }


        checkPassword(mailConfigRequest.getSmtpHostAddress(),
                mailConfigRequest.getSmtpPort(),
                mailConfigRequest.getMailAddress(),
                mailConfigRequest.getMailPassword());

        MailConfig mailConfig = getMailConfigFromRequest(user, mailConfigRequest);

        mailConfigRepository.save(session, mailConfig);

        return ResponseUtil.getResponse(mailConfig.getId());
    }

    @Transactional(value = "projectTransactionManager", rollbackFor = Exception.class)
    public GenericResponse<Long> updateMailConfig(
            @NotNull UserVO user,
            long id,
            @Valid @MandatoryConstraint MailConfigUpdateRequest mailConfigUpdateRequest
    ) throws NotificationException {

        Session session = sessionFactory.getCurrentSession();
        MailConfig mailConfig = getAndCheckMailConfig(session, id, user.getUser_id());

        mailConfigPreCondition.checkUpdateMailConfigArguments(mailConfigUpdateRequest, mailConfig);

        updateMailConfig(mailConfig, mailConfigUpdateRequest);

        checkPassword(
                mailConfig.getHost(),
                Integer.parseInt(mailConfig.getPort()),
                mailConfig.getAddress(),
                AdvancedEncryptionStandard.decrypt(mailConfig.getPassword())
        );

        //todo: update rules based on service name (maybe not necessary due to service name cannot be updated)

        mailConfigRepository.update(session, mailConfig);

        return ResponseUtil.getResponse(mailConfig.getId());
    }


    private MailConfig updateMailConfig(MailConfig mailConfig,
                                        MailConfigUpdateRequest mailConfigUpdateRequest) {

        if (!GeneralUtils.isNullOrEmpty(mailConfigUpdateRequest.getMailAddress())) {

            mailConfig.setAddress(mailConfigUpdateRequest.getMailAddress());
        }

        if (!GeneralUtils.isNullOrZero(mailConfigUpdateRequest.getSmtpPort())) {
            mailConfig.setPort(String.valueOf(mailConfigUpdateRequest.getSmtpPort()));
        }

        if (!GeneralUtils.isNullOrEmpty(mailConfigUpdateRequest.getSmtpHostAddress())) {
            mailConfig.setHost(mailConfigUpdateRequest.getSmtpHostAddress());
        }

        if (!GeneralUtils.isNullOrEmpty(mailConfigUpdateRequest.getNewPassword())) {
            mailConfig.setPassword(AdvancedEncryptionStandard.encrypt(mailConfigUpdateRequest.getNewPassword()));
        }

        return mailConfig;
    }

    private MailConfig getAndCheckMailConfig(Session session, long id, long userId) throws NotificationException {
        return mailConfigRepository.getMailConfigById(session, id, userId)
                .orElseThrow(() -> NotificationException.exception(
                        NotificationExceptionStatus.NOT_FOUND,
                        "تنظیمات ایمیل با شناسه وارد شده موجود نیست."
                ));
    }

    private void checkPassword(String host, int port, String address, String password) throws NotificationException {
        try {
            Properties props = new Properties();

            if (port == 587 || (address.endsWith("yahoo.com")) || (address.endsWith("gmail.com"))) {
                props.setProperty("mail.smtp.starttls.enable", "true");
            } else if (port == 465) {
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            }

            props.setProperty("mail.smtp.port", Integer.toString(port));

            javax.mail.Session session = javax.mail.Session.getInstance(props, null);

            Transport transport = session.getTransport("smtp");

            transport.connect(host, port, address, password);
            transport.close();

        } catch (AuthenticationFailedException e) {
            log.error(e);
            throw NotificationException.exception(
                    NotificationExceptionStatus.INVALID_MAIL_CONFIG,
                    "خطا در اتصال به ایمیل: نام کاربری یا رمز عبور اشتباه است."
            );

        } catch (MessagingException e) {
            log.error(e);
            throw NotificationException.exception(
                    NotificationExceptionStatus.INVALID_MAIL_CONFIG,
                    "هاست وارد شده صحیح نیست"
            );

        } catch (IllegalArgumentException e) {
            log.error(e);
            throw NotificationException.exception(
                    NotificationExceptionStatus.INVALID_MAIL_CONFIG,
                    "پورت وارد شده صحیح نیست."
            );
        }
    }

    private MailConfig getMailConfigFromRequest(UserVO user, MailConfigCreateRequest mailConfigCreateRequest) {
        return MailConfig.builder()
                .name(mailConfigCreateRequest.getServiceName())
                .address(mailConfigCreateRequest.getMailAddress())
                .host(mailConfigCreateRequest.getSmtpHostAddress())
                .port(String.valueOf(mailConfigCreateRequest.getSmtpPort()))
                .password(AdvancedEncryptionStandard.encrypt(mailConfigCreateRequest.getMailPassword()))
                .userId(user.getUser_id())
                .build();
    }

    @Transactional(value = "projectTransactionManager", rollbackFor = Exception.class)
    public GenericResponse<MailConfigSrv> getMailConfig(@NotNull UserVO user, long id) throws NotificationException {
        Session session = sessionFactory.getCurrentSession();

        MailConfig mailConfig = getAndCheckMailConfig(session, id, user.getUser_id());

        return ResponseUtil.getResponse(getMailConfigSrvFromEntity(mailConfig));
    }

    @Transactional(value = "projectTransactionManager", rollbackFor = Exception.class)
    public GenericResponse<List<MailConfigSrv>> getMailConfigs(@NotNull UserVO user,
                                                               @Pagination int size,
                                                               @Pagination int offset) {
        Session session = sessionFactory.getCurrentSession();

        long totalCount = mailConfigRepository.getCountMailConfig(session, user.getUser_id());

        List<MailConfigSrv> result = Collections.emptyList();

        if (totalCount > 0) {
            result = mailConfigRepository.getMailConfigsByOwner(session, user.getUser_id(), size, offset)
                    .stream()
                    .map(this::getMailConfigSrvFromEntity)
                    .collect(Collectors.toList());

        }

        return ResponseUtil.getResponse(result, totalCount);
    }

    private MailConfigSrv getMailConfigSrvFromEntity(MailConfig mailConfig) {
        return MailConfigSrv.builder()
                .id(mailConfig.getId())
                .insertTimestamp(GeneralUtils.getTimestamp(mailConfig.getInsertTime()))
                .updateTimestamp(GeneralUtils.getTimestamp(mailConfig.getUpdateTime()))
                .serviceName(mailConfig.getName())
                .mailAddress(mailConfig.getAddress())
                .smtpHostAddress(mailConfig.getHost())
                .smtpPort(mailConfig.getPort())
                .build();
    }

    @Transactional(value = "projectTransactionManager", rollbackFor = Exception.class)
    public GenericResponse<Long> deleteMailConfig(UserVO user, long id) throws NotificationException {
        Session session = sessionFactory.getCurrentSession();

        MailConfig mailConfig = getAndCheckMailConfig(session, id, user.getUser_id());

        mailConfigRepository.delete(session, mailConfig);

        return ResponseUtil.getResponse(id);
    }


}
