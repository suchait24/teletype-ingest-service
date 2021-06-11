package com.infogain.gcp.poc.consumer.component;
/*
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.api.gax.batching.BatchingSettings;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.threeten.bp.Duration;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class BatchPublisher {

    @Value("${app.topic.name}")
    private String topicName;

    public void publishMessage(List<String> messageList) throws InterruptedException, IOException, JAXBException, ExecutionException {

        Publisher publisher = null;
        List<ApiFuture<String>> messageIdFutures = new ArrayList<>();

        try {
            long requestBytesThreshold = 5000L; // default : 1 byte
            long messageCountBatchSize = 100L; // default : 1 message

            Duration publishDelayThreshold = Duration.ofMillis(100); // default : 1 ms


            BatchingSettings batchingSettings =
                    BatchingSettings.newBuilder()
                            .setElementCountThreshold(messageCountBatchSize)
                            .setRequestByteThreshold(requestBytesThreshold)
                            .setDelayThreshold(publishDelayThreshold)
                            .build();

            publisher = Publisher.newBuilder(topicName).setBatchingSettings(batchingSettings).build();

            Publisher finalPublisher = publisher;

            messageList.forEach(message -> {
                ByteString data = ByteString.copyFromUtf8(message);
                PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();

                ApiFuture<String> messageIdFuture = finalPublisher.publish(pubsubMessage);
                messageIdFutures.add(messageIdFuture);
            });
        } finally {
            List<String> messageIds = ApiFutures.allAsList(messageIdFutures).get();

            log.info("Published " + messageIds.size() + " messages with batch settings.");

            if (publisher != null) {
                publisher.shutdown();
                publisher.awaitTermination(1, TimeUnit.MINUTES);
            }
        }
    }

}
*/