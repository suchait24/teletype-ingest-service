package com.infogain.gcp.poc.consumer.pubsub;

import com.infogain.gcp.poc.consumer.service.PullSubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gcp.pubsub.core.subscriber.PubSubSubscriberTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartProcessing {

    private final PubSubSubscriberTemplate subSubscriberTemplate;
    private final PullSubscriptionService pullDemo;

    //@PostConstruct
    void runPullAlways() {
        log.info("Start pull mechanism in background.");

        //We can use a thread pool as well rather than using single thread.
       Thread subscriberThread = new Thread(() -> {
            while(true) {
                try {
                    pullDemo.pullMessage(subSubscriberTemplate);
                } catch (InterruptedException e) {
                    log.error("Error occurred : {}", e.getMessage());
                } catch (ExecutionException e) {
                    log.error("Error occurred : {}", e.getMessage());
                } catch (JAXBException e) {
                    log.error("Error occurred : {}", e.getMessage());
                } catch (IOException e) {
                    log.error("Error occurred : {}", e.getMessage());
                }
            }
       });

       subscriberThread.setName("subscriber-background-thread");
       subscriberThread.start();
    }
}
