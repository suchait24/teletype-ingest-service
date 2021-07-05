package com.infogain.gcp.poc.consumer.service;

import com.google.cloud.Timestamp;
import com.infogain.gcp.poc.consumer.component.TeletypeMessageStore;
import com.infogain.gcp.poc.consumer.dto.BatchRecord;
import com.infogain.gcp.poc.consumer.dto.TeletypeEventDTO;
import com.infogain.gcp.poc.consumer.entity.TeleTypeEntity;
import com.infogain.gcp.poc.consumer.util.BatchRecordUtil;
import com.infogain.gcp.poc.consumer.util.TeleTypeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gcp.pubsub.support.converter.ConvertedAcknowledgeablePubsubMessage;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class SubscriptionProcessingService {

    private final TeletypeMessageStore teletypeMessageStore;
    private final DuplicateCheckService duplicateCheckService;

    private final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public void processMessages(List<ConvertedAcknowledgeablePubsubMessage<TeletypeEventDTO>> msgs, Timestamp batchReceivedTime, Instant startTime) throws InterruptedException, ExecutionException, IOException, JAXBException {

        if (!msgs.isEmpty()) {
            BatchRecord batchRecord = BatchRecordUtil.createBatchRecord(msgs, batchReceivedTime);
            processSubscriptionMessagesList(batchRecord, startTime);

            //send acknowledge for all processed messages
            msgs.forEach(msg -> msg.ack());
        }
    }

    private void processSubscriptionMessagesList(BatchRecord batchRecord, Instant startTime) {

        AtomicReference<Integer> sequenceNumber = new AtomicReference<>(1);

        List<ConvertedAcknowledgeablePubsubMessage<TeletypeEventDTO>> messageList = null;

        if (!batchRecord.getMessageList().isEmpty())
            messageList = batchRecord.getMessageList();

        //log.info("Started processing subscription messages list , total records found : {}", messageList.size());

        List<TeleTypeEntity> teleTypeEntityList = messageList.stream()
                .map(message -> wrapTeletypeConversionException(message))
                .collect(Collectors.toList());

        teletypeMessageStore.saveMessagesList(teleTypeEntityList);

        //log.info("Processing stopped, all records processed  : {}", teleTypeEntityList.size());

        Instant end = Instant.now();
        Long totalTime = Duration.between(startTime, end).toMillis();
        log.info("total time taken to process {} records is {} ms", teleTypeEntityList.size(), totalTime);
    }

    private TeleTypeEntity wrapTeletypeConversionException(ConvertedAcknowledgeablePubsubMessage<TeletypeEventDTO> message) {

        String uniqueId = duplicateCheckService.getTasUniqueId();

        try {
            return TeleTypeUtil.convert(message, TeleTypeUtil.marshall(message.getPayload()), TeleTypeUtil.toJsonString(message.getPayload()), uniqueId);
        } catch (JAXBException e) {
            log.error("error occurred while converting : {}", e.getMessage());
        }
        return null;
    }

}
