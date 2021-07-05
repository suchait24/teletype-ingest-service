package com.infogain.gcp.poc.consumer.service;

import com.google.pubsub.v1.ReceivedMessage;
import com.infogain.gcp.poc.consumer.component.PubSubSubscriber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class PullSubscriptionService {

    @Value("${spring.cloud.gcp.project-id}")
    private String projectId;
    @Value("${app.subscription.id}")
    private String subscriptionId;

    private final PubSubSubscriber pubSubSubscriber;
    private final SubscriptionProcessingService subscriptionProcessingService;

    public void pullMessages() throws InterruptedException, ExecutionException, JAXBException, IOException {

        Instant startTime = Instant.now();
        List<ReceivedMessage> receivedMessageList = pubSubSubscriber.getPullResponse();

        //acknowledge only when batch is successfully processed.
        if(!receivedMessageList.isEmpty()) {

            log.info("Message list of size : {} has been pulled.", receivedMessageList.size());
            LocalDateTime batchReceivedTime = LocalDateTime.now();
            List<String> ackIds = subscriptionProcessingService.processMessages(receivedMessageList, batchReceivedTime,  startTime);
            pubSubSubscriber.acknowledgeMessageList(ackIds);
        }
    }

}
