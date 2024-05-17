package ir.fum.cloud.notification.core.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class MailProcessorFactory {

    private final AsyncMailProcessor asyncMailProcessor;


    public MailProcessor getMailProcessor(boolean isBulk) {
        return asyncMailProcessor;
    }

}