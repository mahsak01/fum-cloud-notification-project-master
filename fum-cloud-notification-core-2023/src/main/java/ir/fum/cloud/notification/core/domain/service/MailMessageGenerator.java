package ir.fum.cloud.notification.core.domain.service;

import ir.fum.cloud.notification.core.configuration.MailPropertiesConfiguration;
import ir.fum.cloud.notification.core.domain.model.request.SendMailRequest;
import ir.fum.cloud.notification.core.domain.model.vo.SendMailRequestMetadata;
import ir.fum.cloud.notification.core.domain.model.vo.SendMailVO;
import ir.fum.cloud.notification.core.util.GeneralUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class MailMessageGenerator {
    private final MailPropertiesConfiguration mailPropertiesConfiguration;


    public Message generate(Session session, SendMailVO sendMailVO) throws MessagingException {
        Message message = new MimeMessage(session);

        message.setFrom(new InternetAddress(sendMailVO.getMailConfig().getAddress()));

        setReceivers(message, sendMailVO.getRequestInformation().values());

        if (message.getRecipients(Message.RecipientType.TO).length > 0) {
            setCC(message, sendMailVO.getSendMailRequest().getCc());

            setBCC(message, sendMailVO.getSendMailRequest().getBcc());

            setReplyAddress(
                    message,
                    GeneralUtils.isNullOrEmpty(sendMailVO.getSendMailRequest().getReplayAddress()) ?
                            sendMailVO.getMailConfig().getAddress() : sendMailVO.getSendMailRequest().getReplayAddress()
            );

            setMailSubject(message, sendMailVO.getSendMailRequest().getSubject());

            setContent(message, sendMailVO.getSendMailRequest(), sendMailVO.getFileNames(), sendMailVO.isResend());
        }

        return message;
    }

    private void setReceivers(Message message, Collection<SendMailRequestMetadata> infos) throws MessagingException {
        List<Address> addresses = new ArrayList<>();

        for (SendMailRequestMetadata info : infos) {

            if (info.getState() == null) {
                addresses.add(new InternetAddress(info.getReceiver()));
            }
        }

        message.addRecipients(Message.RecipientType.TO, addresses.toArray(new Address[0]));
    }

    private void setCC(Message message, List<String> cc) throws MessagingException {
        if (!GeneralUtils.isNullOrEmpty(cc)) {
            List<Address> addresses = new ArrayList<>();

            for (String address : cc) {
                addresses.add(new InternetAddress(address));
            }

            message.addRecipients(Message.RecipientType.CC, addresses.toArray(new Address[0]));

        }
    }

    private void setBCC(Message message, List<String> bcc) throws MessagingException {
        if (!GeneralUtils.isNullOrEmpty(bcc)) {
            List<Address> addresses = new ArrayList<>();

            for (String address : bcc) {
                addresses.add(new InternetAddress(address));
            }

            message.addRecipients(Message.RecipientType.BCC, addresses.toArray(new Address[0]));

        }
    }

    private void setReplyAddress(Message message, String replyAddress) throws MessagingException {
        message.setReplyTo(new Address[]{new InternetAddress(replyAddress)});
    }

    private void setMailSubject(Message message, String subject) throws MessagingException {
        message.setSubject(subject);

    }

    private void setContent(Message message,
                            SendMailRequest sendMailRequest,
                            List<String> fileNames,
                            boolean resend) throws MessagingException {

        MimeMultipart multipart = new MimeMultipart();
        MimeBodyPart messageBodyPart = new MimeBodyPart();

        if (resend) {
            messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(sendMailRequest.getContent(), "text/html; charset=UTF-8");
            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart);
            return;
        }

        if (!GeneralUtils.isNullOrEmpty(sendMailRequest.getPlainText())) {
            messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(sendMailRequest.getPlainText(), "utf-8");
            multipart.addBodyPart(messageBodyPart);
        }

        if (!GeneralUtils.isNullOrEmpty(sendMailRequest.getContent())) {
            messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(sendMailRequest.getContent(), "text/html; charset=UTF-8");
            multipart.addBodyPart(messageBodyPart);

        } else if (GeneralUtils.isNullOrEmpty(sendMailRequest.getPlainText())) {
            messageBodyPart.setText("no content", "utf-8");
            multipart.addBodyPart(messageBodyPart);
        }

        setFileHashes(multipart, sendMailRequest, fileNames);

        message.setContent(multipart);
    }


    private void setFileHashes(MimeMultipart multipart, SendMailRequest sendMailRequest, List<String> fileNames) throws MessagingException {
        if (fileNames != null) {

            StringBuilder files = new StringBuilder();

            String attachmentStyle = mailPropertiesConfiguration.getAttachmentStyle();
            String fileStyle = mailPropertiesConfiguration.getFileStyle();


            IntStream.range(0, fileNames.size())
                    .forEach(i -> {
                        String style = fileStyle;

                        style = style.replace("{{fileName}}", fileNames.get(i));

                        files.append(style);
                    });

            attachmentStyle = attachmentStyle.replace("{{files}}", files.toString());


            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(attachmentStyle, "text/html; charset=UTF-8");
            multipart.addBodyPart(messageBodyPart);

            sendMailRequest.setContent(sendMailRequest.getContent() + attachmentStyle);

        }
    }


}
