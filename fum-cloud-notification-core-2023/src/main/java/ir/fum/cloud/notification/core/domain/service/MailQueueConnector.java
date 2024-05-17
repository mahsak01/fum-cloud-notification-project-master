package ir.fum.cloud.notification.core.domain.service;

import ir.fum.cloud.notification.core.data.hibernate.entity.MailConfig;
import ir.fum.cloud.notification.core.data.hibernate.entity.MailRequest;
import ir.fum.cloud.notification.core.data.hibernate.repository.MailConfigRepository;
import ir.fum.cloud.notification.core.data.hibernate.repository.MailRequestRepository;
import ir.fum.cloud.notification.core.domain.model.vo.MailQueueVO;
import ir.fum.cloud.notification.core.domain.model.vo.SendMailVO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;


@RequiredArgsConstructor
public abstract class MailQueueConnector {

    private final MailConfigRepository mailConfigRepository;
    private final MailRequestRepository mailRequestRepository;


    protected MailQueueVO generateMailQueueVO(SendMailVO sendMailVO) {


        String requestId = sendMailVO.getRequestId();
        long configId = sendMailVO.getMailConfig().getId();


        sendMailVO.setMailConfig(null);

        return new MailQueueVO(sendMailVO, configId, requestId);
    }

    protected Pair<SendMailVO, MailRequest> getMailInfoFromQueueVO(MailQueueVO mailQueueVO) {
        MailRequest mailRequest = mailRequestRepository.findByRequestId(mailQueueVO.getRequestId());


        MailConfig mailConfig = mailConfigRepository.getMailConfigById(mailQueueVO.getConfigId())
                .orElse(null);

        SendMailVO sendMailVO = mailQueueVO.getSendMailVO();

        sendMailVO.setMailConfig(mailConfig);

        return Pair.of(sendMailVO, mailRequest);
    }

}
