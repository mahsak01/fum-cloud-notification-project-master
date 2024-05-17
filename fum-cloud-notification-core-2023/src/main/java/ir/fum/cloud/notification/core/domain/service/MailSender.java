package ir.fum.cloud.notification.core.domain.service;


import com.fasterxml.jackson.core.type.TypeReference;
import ir.fum.cloud.notification.core.configuration.MailResendPropertiesConfiguration;
import ir.fum.cloud.notification.core.data.hibernate.entity.MailConfig;
import ir.fum.cloud.notification.core.data.hibernate.entity.MailItem;
import ir.fum.cloud.notification.core.data.hibernate.entity.MailRequest;
import ir.fum.cloud.notification.core.data.hibernate.entity.model.RequestState;
import ir.fum.cloud.notification.core.data.hibernate.repository.MailItemRepository;
import ir.fum.cloud.notification.core.domain.model.vo.SendMailRequestMetadata;
import ir.fum.cloud.notification.core.domain.model.vo.SendMailVO;
import ir.fum.cloud.notification.core.exception.NotificationException;
import ir.fum.cloud.notification.core.exception.NotificationExceptionStatus;
import ir.fum.cloud.notification.core.util.AdvancedEncryptionStandard;
import ir.fum.cloud.notification.core.util.GeneralUtils;
import ir.fum.cloud.notification.core.util.mapper.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Log4j2
public class MailSender {
    private final MailMessageGenerator mailMessageGenerator;

    private final SessionFactory sessionFactory;


    private final MailItemRepository mailItemRepository;


    private final MailResendService mailResendService;

    private final MailResendPropertiesConfiguration mailResendPropertiesConfiguration;

    @Transactional(value = "projectTransactionManager", rollbackFor = Exception.class)
    public void sendMail(@NotNull SendMailVO sendMailVO,
                         MailRequest mailRequest) throws NotificationException {
        MailConfig config = sendMailVO.getMailConfig();

        Properties sendProperties = getSendProperties(config.getHost(), config.getPort(), config.getAddress());

        String username = config.getAddress().substring(0, config.getAddress().indexOf('@'));

        org.hibernate.Session hibernateSession = sessionFactory.getCurrentSession();


        try {
            if (!GeneralUtils.isNullOrEmpty(username) && !GeneralUtils.isNullOrEmpty(sendMailVO.getReceivers())) {
                sendMail(sendMailVO, config, sendProperties);

                sendMailVO.getRequestInformation()
                        .forEach((messageId, info) -> {
                            if (info.getState() == null) {
                                info.setState(RequestState.SENT);
                            }
                        });
            }

            saveOrUpdate(hibernateSession, mailRequest, sendMailVO);

        } catch (AddressException e) {
            log.error("Address Error {} ", e.getMessage());

            if (sendMailVO.isThrowException()) {
                throw NotificationException.exception(
                        NotificationExceptionStatus.INVALID_INPUT,
                        "خطا در آدرس های وارد شده."
                );
            }

            sendMailVO.getRequestInformation()
                    .forEach((messageId, info) -> {
                        if (info.getState() == null) {
                            info.setState(RequestState.NOT_SENT);
                            info.addToMetadata("error", e.getMessage());
                        }
                    });

            saveOrUpdate(hibernateSession, mailRequest, sendMailVO);

        } catch (MessagingException e) {
            log.error("Messaging Error {} ", e.getMessage());

            boolean resendRequested = sendMailVO.getSendMailRequest().isResend() &&
                    sendMailVO.getResendCounter() > mailResendPropertiesConfiguration.getRetryLimit() &&
                    !e.getMessage().contains("message content rejected");

            if (sendMailVO.isThrowException() && !resendRequested) {
                throw NotificationException.exception(
                        NotificationExceptionStatus.INTERNAL_ERROR,
                        "خطایی در ارسال رخ داده است."
                );
            }

            sendMailVO.getRequestInformation()
                    .forEach((messageId, info) -> {
                        if (info.getState() == null) {
                            setErrorAndStateForRequestMetadata(
                                    info,
                                    resendRequested ? RequestState.RESEND_QUEUE : RequestState.NOT_SENT,
                                    e.getMessage()
                            );
                        }
                    });

            saveOrUpdate(hibernateSession, mailRequest, sendMailVO);

            if (resendRequested) {
                mailResendService.resend(sendMailVO);
            }

        }

    }

    private void setErrorAndStateForRequestMetadata(SendMailRequestMetadata info, RequestState state, String message) {
        info.setState(state);
        info.addToMetadata("error", message);
    }

    private void sendMail(SendMailVO sendMailVO, MailConfig config, Properties sendProperties) throws MessagingException {
        Session transportSession = getTransportSession(
                sendProperties,
                config.getAddress(),
                AdvancedEncryptionStandard.decrypt(config.getPassword()));

        Message message = mailMessageGenerator.generate(transportSession, sendMailVO);

        if (message.getRecipients(Message.RecipientType.TO).length > 0) {
            Transport.send(message);
        }
    }

    private void saveOrUpdate(org.hibernate.Session session,
                              MailRequest mailRequest,
                              SendMailVO sendMailVO) {
        if (sendMailVO.isResend()) {
            updateMail(session, sendMailVO, LocalDateTime.now());
        } else {
            saveMail(session, mailRequest, LocalDateTime.now(), sendMailVO);
        }
    }

    private void updateMail(org.hibernate.Session session, SendMailVO sendMailVO, LocalDateTime sendDate) {
        List<MailItem> mailItems = mailItemRepository.findByMessageId(
                session,
                new ArrayList<>(sendMailVO.getRequestInformation().keySet())
        );

        mailItems.forEach(mailItem -> {
            String messageId = mailItem.getMessageId();

            updateMailItem(session, mailItem, sendMailVO.getRequestInformation().get(messageId), sendDate);


        });


    }

    private void updateMailItem(org.hibernate.Session session,
                                MailItem mailItem,
                                SendMailRequestMetadata info,
                                LocalDateTime sendDate) {

        Map<String, Object> metadata = JsonUtils.getObject(
                mailItem.getMetaData(),
                new TypeReference<Map<String, Object>>() {
                });

        metadata.remove("error");

        metadata.putAll(info.getMetadata());

        mailItem.setState(info.getState());
        mailItem.setSendDate(sendDate);
        mailItem.setMetaData(JsonUtils.getStringJson(metadata));

        mailItemRepository.update(session, mailItem);

    }

    private void saveMail(org.hibernate.Session session,
                          MailRequest mailRequest,
                          LocalDateTime sendTime,
                          SendMailVO sendMailVO) {

        List<MailItem> mails = generateMailItem(sendMailVO, mailRequest, sendTime);

        mails.forEach(mailItem -> mailItemRepository.save(session, mailItem));

    }

    private List<MailItem> generateMailItem(SendMailVO sendMailVO,
                                            MailRequest mailRequest,
                                            LocalDateTime sendTime) {

        return sendMailVO.getRequestInformation()
                .entrySet()
                .stream()
                .map(entry -> MailItem.builder()
                        .state(entry.getValue().getState())
                        .sendDate(sendTime)
                        .receiver(entry.getValue().getReceiver())
                        .metaData(JsonUtils.getStringJson(entry.getValue().getMetadata()))
                        .mailRequest(mailRequest)
                        .messageId(entry.getKey())
                        .build())
                .collect(Collectors.toList());
    }


    private Session getTransportSession(Properties sendMailProperties, String address, String password) {
        return Session.getInstance(sendMailProperties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(address, password);
                    }
                });
    }

    private Properties getSendProperties(String host, String port, String address) {
        Properties properties = new Properties();

        properties.setProperty("mail.smtp.auth", "true");

        if (port.equalsIgnoreCase("587")) {
            properties.setProperty("mail.smtp.starttls.enable", "true");
            properties.setProperty("mail.smtp.port", "587");

        } else if (port.equalsIgnoreCase("465")) {
            properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            properties.setProperty("mail.smtp.port", "465");

        } else {
            properties.setProperty("mail.smtp.port", port);
            properties.setProperty("mail.smtp.starttls.enable", "true");
        }

        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.ssl.trust", host);
        properties.setProperty("emailAddress", address);
        properties.setProperty("mail.store.protocol", "imaps");
        properties.setProperty("mail.imap.ssl.trust", host);
        properties.setProperty("mail.imap.port", "143");
        properties.setProperty("mail.imap.starttls.enable", "true");
        properties.setProperty("mail.imap.auth", "true");
        properties.setProperty("session.getStore", "imap");

        return properties;
    }

}
