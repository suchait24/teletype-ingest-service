package com.infogain.gcp.poc.consumer.util;

import com.google.cloud.Timestamp;
import com.infogain.gcp.poc.consumer.dto.BatchRecord;
import com.infogain.gcp.poc.consumer.dto.TeletypeEventDTO;
import org.springframework.cloud.gcp.pubsub.support.converter.ConvertedAcknowledgeablePubsubMessage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

public class BatchRecordUtil {

    private static Integer BATCH_MESSAGE_ID = 1;

    public static BatchRecord createBatchRecord(List<TeletypeEventDTO> teletypeEventDTOList, LocalDateTime batchReceivedTime) {

        BatchRecord batchRecord = new BatchRecord();
        batchRecord.setDtoList(teletypeEventDTOList);
        batchRecord.setBatchMessageId(BATCH_MESSAGE_ID++);
        batchRecord.setBatchReceivedTime(batchReceivedTime);

        return batchRecord;
    }

}
