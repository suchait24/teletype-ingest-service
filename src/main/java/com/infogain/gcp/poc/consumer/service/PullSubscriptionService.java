package com.infogain.gcp.poc.consumer.service;

import com.google.cloud.Timestamp;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.infogain.gcp.poc.consumer.dto.BatchRecord;
import com.infogain.gcp.poc.consumer.dto.TeletypeEventDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gcp.pubsub.core.subscriber.PubSubSubscriberTemplate;
import org.springframework.cloud.gcp.pubsub.support.converter.ConvertedAcknowledgeablePubsubMessage;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PullSubscriptionService {

    @Value("${spring.cloud.gcp.project-id}")
    private String projectId;
    @Value("${app.subscription.id}")
    private String subscriptionId;

    private final SubscriptionProcessingService subscriptionProcessingService;
    private static Integer BATCH_MESSAGE_ID = 1;


    public void pullMessage(PubSubSubscriberTemplate subscriberTemplate) {

        List<ConvertedAcknowledgeablePubsubMessage<TeletypeEventDTO>> msgs = subscriberTemplate
                .pullAndConvert(ProjectSubscriptionName.of(projectId, subscriptionId).toString(), 100, true, TeletypeEventDTO.class);

        Timestamp batchReceivedTime = Timestamp.now();

        msgs.forEach(msg -> {
            log.info("message received : {}", msg.getPayload().toString());
            msg.ack();
        });

        if (!msgs.isEmpty()) {
            List<TeletypeEventDTO> teletypeEventDTOList = msgs.stream().map(msg -> msg.getPayload()).collect(Collectors.toList());
            BatchRecord batchRecord = createBatchRecord(teletypeEventDTOList, batchReceivedTime);
            subscriptionProcessingService.processSubscriptionMessagesList(batchRecord);
        }
    }

    private BatchRecord createBatchRecord(List<TeletypeEventDTO> teletypeEventDTOList, Timestamp batchReceivedTime) {

        Random random = new Random();

        BatchRecord batchRecord = new BatchRecord();
        batchRecord.setDtoList(teletypeEventDTOList);
        //batchRecord.setBatchMessageId(random.nextInt(7));
        batchRecord.setBatchMessageId(BATCH_MESSAGE_ID++);
        batchRecord.setBatchReceivedTime(batchReceivedTime);

        return batchRecord;
    }
}
