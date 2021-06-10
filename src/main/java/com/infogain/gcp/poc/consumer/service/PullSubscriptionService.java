package com.infogain.gcp.poc.consumer.service;

import com.google.cloud.Timestamp;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.infogain.gcp.poc.consumer.dto.TeletypeEventDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gcp.pubsub.core.subscriber.PubSubSubscriberTemplate;
import org.springframework.cloud.gcp.pubsub.support.converter.ConvertedAcknowledgeablePubsubMessage;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;
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

    private final SubscriptionProcessingService subscriptionProcessingService;

    public void pullMessage(PubSubSubscriberTemplate subscriberTemplate) throws InterruptedException, ExecutionException, JAXBException, IOException {

        List<ConvertedAcknowledgeablePubsubMessage<TeletypeEventDTO>> msgs = subscriberTemplate
                .pullAndConvert(ProjectSubscriptionName.of(projectId, subscriptionId).toString(), 100, true, TeletypeEventDTO.class);

        Timestamp batchReceivedTime = Timestamp.now();

        //acknowledge only when batch is successfully processed.
        subscriptionProcessingService.processMessages(msgs, batchReceivedTime);

    }

}
