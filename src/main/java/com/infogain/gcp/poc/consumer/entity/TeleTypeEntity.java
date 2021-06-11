package com.infogain.gcp.poc.consumer.entity;

import com.google.cloud.Timestamp;
import lombok.*;
import org.springframework.cloud.gcp.data.spanner.core.mapping.Column;
import org.springframework.cloud.gcp.data.spanner.core.mapping.PrimaryKey;
import org.springframework.cloud.gcp.data.spanner.core.mapping.Table;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Table(name = "TAS")
public class TeleTypeEntity {

    @PrimaryKey
    @Column(name = "TAS_ID")
    @Id
    private String tasId;

    @Column(name = "HOST_LOCATOR")
    private String hostLocator;

    @Column(name = "MESSAGE_CORRELATION_ID")
    private String messageCorrelationId;

    @Column(name = "CARRIER_CODE")
    private String carrierCode;

    @Column(name = "CREATED_TIMESTAMP")
    private Timestamp createdTimestamp;

    @Column(name = "UPDATED_TIMESTAMP")
    private Timestamp updatedTimestamp;

    @Column(name = "SEQUENCE_NUMBER")
    private Long sequenceNumber;

    @Column(name = "PAYLOAD")
    private String payload;

    @Column(name = "BATCH_ID")
    private Integer batchId;

    @Column(name = "PAYLOAD_JSON")
    private String payloadJson;
}
