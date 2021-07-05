package com.infogain.gcp.poc.consumer.service;

import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.ReceivedMessage;
import com.infogain.gcp.poc.consumer.component.TeletypeMessageStore;
import com.infogain.gcp.poc.consumer.dto.BatchRecord;
import com.infogain.gcp.poc.consumer.dto.TeletypeEventDTO;
import com.infogain.gcp.poc.consumer.entity.TeleTypeEntity;
import com.infogain.gcp.poc.consumer.util.BatchRecordUtil;
import com.infogain.gcp.poc.consumer.util.PubSubMessageHelper;
import com.infogain.gcp.poc.consumer.util.TeleTypeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gcp.pubsub.support.converter.ConvertedAcknowledgeablePubsubMessage;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
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

    public List<String> processMessages(List<ReceivedMessage> receivedMessageList, LocalDateTime batchReceivedTime, Instant startTime) throws InterruptedException, ExecutionException, IOException, JAXBException {

        List<TeletypeEventDTO> teletypeEventDTOList = retrieveTeletypeEventDTOList(receivedMessageList);

        BatchRecord batchRecord = BatchRecordUtil.createBatchRecord(teletypeEventDTOList, batchReceivedTime);

        //send acknowledge for all processed messages
       return receivedMessageList.stream()
                .map(msg -> msg.getAckId())
                .collect(Collectors.toList());
    }

    private void processSubscriptionMessagesList(BatchRecord batchRecord, Instant startTime) {

        AtomicReference<Integer> sequenceNumber = new AtomicReference<>(1);

        List<TeletypeEventDTO> teletypeEventDTOList = null;

        if (!batchRecord.getDtoList().isEmpty())
            teletypeEventDTOList = batchRecord.getDtoList();

        List<TeleTypeEntity> teletypeEventDTOMessages = preparePubSubMessageList(teletypeEventDTOList, sequenceNumber, batchRecord);

        teletypeMessageStore.saveMessagesList(teletypeEventDTOMessages);

        //log.info("Processing stopped, all records processed  : {}", teleTypeEntityList.size());

        Instant end = Instant.now();
        Long totalTime = Duration.between(startTime, end).toMillis();
        log.info("total time taken to process {} records is {} ms", teletypeEventDTOMessages.size(), totalTime);
    }

    private TeleTypeEntity wrapTeletypeConversionException(TeletypeEventDTO teletypeEventDTO, Integer sequenceNumber, Integer batchId)  {

        String uniqueId = duplicateCheckService.getTasUniqueId();

        try {
            return PubSubMessageHelper.getPubSubMessage(teletypeEventDTO, sequenceNumber, batchId, uniqueId);
        } catch (JAXBException e) {
            log.error("Exception during marshalling : {}", e.getMessage());
        }
        return null;
    }

    private List<TeletypeEventDTO> retrieveTeletypeEventDTOList(List<ReceivedMessage> receivedMessageList) {

        //TODO - fix this once tested
        return receivedMessageList.stream().map(msg -> {
            try {
                return TeleTypeUtil.unmarshall(msg.getMessage().getData().toStringUtf8());
            } catch (JAXBException e) {
                log.error("error occurred : {}", e.getMessage());
            }
            return null;
        }).collect(Collectors.toList());
    }

    private List<TeleTypeEntity> preparePubSubMessageList(List<TeletypeEventDTO> teletypeEventDTOList, AtomicReference<Integer> sequenceNumber, BatchRecord batchRecord) {

        return teletypeEventDTOList.stream()
                .map(record -> wrapTeletypeConversionException(record, sequenceNumber.getAndSet(sequenceNumber.get() + 1), batchRecord.getBatchMessageId()))
                .collect(Collectors.toList());
    }

}
