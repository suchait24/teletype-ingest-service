package com.infogain.gcp.poc.consumer.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class BatchRecord {

    private List<TeletypeEventDTO> dtoList;
    private Integer batchMessageId;
    private LocalDateTime batchReceivedTime;
}
