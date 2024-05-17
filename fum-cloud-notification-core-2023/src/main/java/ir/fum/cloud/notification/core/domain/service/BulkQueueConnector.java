package ir.fum.cloud.notification.core.domain.service;


import ir.fum.cloud.notification.core.data.hibernate.entity.MailRequest;
import ir.fum.cloud.notification.core.data.hibernate.repository.MailConfigRepository;
import ir.fum.cloud.notification.core.data.hibernate.repository.MailRequestRepository;
import ir.fum.cloud.notification.core.domain.model.vo.MailQueueVO;
import ir.fum.cloud.notification.core.domain.model.vo.SendMailVO;
import ir.fum.cloud.notification.core.exception.NotificationException;
import ir.fum.cloud.notification.core.queue.MailBulkProducer;
import ir.fum.cloud.notification.core.util.mapper.JsonUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class BulkQueueConnector extends MailQueueConnector {

    private final MailSender mailSender;
    private final MailBulkProducer mailBulkProducer;

    public BulkQueueConnector(MailConfigRepository mailConfigRepository,
                              MailRequestRepository mailRequestRepository,
                              MailSender mailSender,
                              MailBulkProducer mailBulkProducer) {

        super(mailConfigRepository, mailRequestRepository);
        this.mailSender = mailSender;
        this.mailBulkProducer = mailBulkProducer;
    }


    public void sendToQueue(SendMailVO sendMailVO) {
        try {
            mailBulkProducer.sendMessage(
                    JsonUtils.getStringJson(generateMailQueueVO(sendMailVO)),
                    0L
            );
        } catch (Exception e) {
            log.error(e);
        }
    }

    public void handleBulkItem(MailQueueVO mailQueueVO) {
        try {


            Pair<SendMailVO, MailRequest> info = getMailInfoFromQueueVO(mailQueueVO);

            mailSender.sendMail(info.getLeft(), info.getRight());

        } catch (NotificationException e) {
            log.error("CRA-NOT: error on sending bulk item {} ", e.getDeveloperMessage());
        }
    }
}
