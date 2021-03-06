package com.infogain.gcp.poc.consumer.util;

import com.google.cloud.Timestamp;
import com.google.gson.Gson;
import com.infogain.gcp.poc.consumer.dto.TeletypeEventDTO;
import com.infogain.gcp.poc.consumer.entity.TeleTypeEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gcp.pubsub.support.converter.ConvertedAcknowledgeablePubsubMessage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
public class TeleTypeUtil {

    private static final String BATCH_ID = "batch_id";
    private static final String SEQUENCE_NUMBER = "sequence_number";
    private static final String CREATED_TIME = "created_time";

    public static TeletypeEventDTO unmarshall(String message) throws JAXBException {

        JAXBContext jaxbContext = JAXBContext.newInstance(TeletypeEventDTO.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        TeletypeEventDTO teletypeEventDTO = (TeletypeEventDTO) unmarshaller.unmarshal(new StringReader(message));

        return teletypeEventDTO;
    }

    public static String marshall(TeletypeEventDTO teletypeEventDTO) throws JAXBException {

        JAXBContext jaxbContext = JAXBContext.newInstance(TeletypeEventDTO.class);
        Marshaller marshaller = jaxbContext.createMarshaller();

        StringWriter stringWriter = new StringWriter();
        marshaller.marshal(teletypeEventDTO, stringWriter);

        String result = stringWriter.toString();
        log.info("Teletype XML generated : {}", result);

        return result;
    }

    public static String toJsonString(TeletypeEventDTO teletypeEventDTO) {
        return new Gson().toJson(teletypeEventDTO);
    }

    public static TeleTypeEntity convert(ConvertedAcknowledgeablePubsubMessage<TeletypeEventDTO> message, String messageXml, String messageJson, String uniqueId) {

        /*
        String createdTime = message.getPubsubMessage().getAttributesOrDefault(CREATED_TIME, "default-value");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(createdTime, formatter);
        log.info("local date time : {}", localDateTime);

         */

        log.info("message : {}", message);

        return TeleTypeEntity.builder()
                .tasId(uniqueId)
                .hostLocator(message.getPayload().getHostRecordLocator())
                .carrierCode(message.getPayload().getCarrierCode())
                .messageCorrelationId(String.valueOf(message.getPayload().getMessageCorelationId()))
                .sequenceNumber(Long.valueOf(message.getPubsubMessage().getAttributesOrDefault(SEQUENCE_NUMBER, "default-value")))
                .createdTimestamp(LocalDateTime.now())
                .batchId(Integer.valueOf(message.getPubsubMessage().getAttributesOrDefault(BATCH_ID, "default-value")))
                .payload(messageXml)
                .payloadJson(messageJson)
                .build();
    }
}
