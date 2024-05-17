package ir.fum.cloud.notification.core.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created By F.Khojasteh on 5/11/2021
 */

@Configuration
@EnableAsync
public class SpringAsyncConfig implements AsyncConfigurer {
    @Value("${async.thread.pool.size: 200}")
    private int ASYNC_POOL_SIZE;

    @Value("${mail.bulk.thread.pool: 200}")
    private int MAIL_BULK_POOL_SIZE;

    @Bean(name = "async.thread.pool")
    public ExecutorService asyncExecutor() {
        return Executors.newFixedThreadPool(ASYNC_POOL_SIZE);
    }

    @Bean(name = "mail.bulk.thread.pool")
    public ExecutorService mailExecutor() {
        return Executors.newFixedThreadPool(MAIL_BULK_POOL_SIZE);
    }

}
