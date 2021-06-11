package com.infogain.gcp.poc.consumer.service;

import com.google.cloud.Timestamp;
import com.infogain.gcp.poc.consumer.component.BatchStore;
import com.infogain.gcp.poc.consumer.component.TeletypeMessageStore;
import com.infogain.gcp.poc.consumer.dto.BatchRecord;
import com.infogain.gcp.poc.consumer.dto.TeletypeEventDTO;
import com.infogain.gcp.poc.consumer.entity.BatchEventEntity;
import com.infogain.gcp.poc.consumer.entity.TeleTypeEntity;
import com.infogain.gcp.poc.consumer.util.BatchEventEntityUtil;
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

    private static final String SUBSCRIBER_ID = "S1";
    private final TeletypeMessageStore teletypeMessageStore;
    private final BatchStore batchStore;

    private final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public void processMessages(List<ConvertedAcknowledgeablePubsubMessage<TeletypeEventDTO>> msgs, Timestamp batchReceivedTime) throws InterruptedException, ExecutionException, IOException, JAXBException {

        if (!msgs.isEmpty()) {
            List<TeletypeEventDTO> teletypeEventDTOList = msgs.stream().map(msg -> msg.getPayload()).collect(Collectors.toList());
            BatchRecord batchRecord = BatchRecordUtil.createBatchRecord(teletypeEventDTOList, batchReceivedTime);
            processSubscriptionMessagesList(batchRecord);

            //send acknowledge for all processed messages
            msgs.forEach(msg -> msg.ack());

        }
    }

    private void processSubscriptionMessagesList(BatchRecord batchRecord) {

        AtomicReference<Integer> sequenceNumber = new AtomicReference<>(1);
        Instant start = Instant.now();

        List<TeletypeEventDTO> teletypeEventDTOList = null;

        if (!batchRecord.getDtoList().isEmpty())
            teletypeEventDTOList = batchRecord.getDtoList();

        log.info("Started processing subscription messages list , total records found : {}", teletypeEventDTOList.size());

        List<TeleTypeEntity> teleTypeEntityList = teletypeEventDTOList.stream()
                .map(record -> wrapTeletypeConversionException(record))
                .collect(Collectors.toList());

        teletypeMessageStore.saveMessagesList(teleTypeEntityList);

        log.info("Processing stopped, all records processed  : {}", teletypeEventDTOList.size());

        log.info("Logging batch to database now.");
        BatchEventEntity batchEventEntity = BatchEventEntityUtil.createBatchEventEntity(teleTypeEntityList, batchRecord, SUBSCRIBER_ID);
        log.info("Batch entity generated : {}", batchEventEntity);

        batchStore.saveBatchEventEntity(batchEventEntity);

        Instant end = Instant.now();
        log.info("total time taken to process {} records is {} ms", teletypeEventDTOList.size(), Duration.between(start, end).toMillis());
    }

    private TeleTypeEntity wrapTeletypeConversionException(TeletypeEventDTO teletypeEventDTO) {

        try {
            return TeleTypeUtil.convert(teletypeEventDTO, TeleTypeUtil.marshall(teletypeEventDTO), TeleTypeUtil.toJsonString(teletypeEventDTO));
        } catch (JAXBException e) {
            log.error("error occurred while converting : {}", e.getMessage());
        }
        return null;
    }

}
