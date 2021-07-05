package com.infogain.gcp.poc.consumer.util;

import com.google.gson.Gson;
import com.infogain.gcp.poc.consumer.dto.TeletypeEventDTO;
import com.infogain.gcp.poc.consumer.entity.TeleTypeEntity;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDateTime;


@Slf4j
public class TeleTypeUtil {

    private static final String BATCH_ID = "batch_id";
    private static final String SEQUENCE_NUMBER = "sequence_number";
    private static final String CREATED_TIME = "created_time";
    private static Integer sequenceNumber = 0;

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
        //log.info("Teletype XML generated : {}", result);

        return result;
    }

    public static String toJsonString(TeletypeEventDTO teletypeEventDTO) {
        return new Gson().toJson(teletypeEventDTO);
    }

    /*
    public static TeleTypeEntity convert(ConvertedAcknowledgeablePubsubMessage<TeletypeEventDTO> message, String messageXml, String messageJson, String uniqueId) {

        //log.info("Message : {}", message);

        return TeleTypeEntity.builder()
                .tasId(uniqueId)
                .hostLocator(message.getPayload().getMessageIdentity())
                .carrierCode(message.getPayload().getOrigin())
                .messageCorrelationId(String.valueOf(message.getPayload().getMessageCorrelationID()))
                .sequenceNumber(Long.valueOf(message.getPubsubMessage().getAttributesOrDefault(SEQUENCE_NUMBER, String.valueOf(sequenceNumber++))))
                .createdTimestamp(LocalDateTime.now())
                .batchId(Integer.valueOf(message.getPubsubMessage().getAttributesOrDefault(BATCH_ID, "10")))
                .payload(messageXml)
                .payloadJson(messageJson)
                .build();
    }

     */

    public static TeleTypeEntity convert2(TeletypeEventDTO teletypeEventDTO, String messageXml, String messageJson, String uniqueId) {

        //log.info("Message : {}", message);

        return TeleTypeEntity.builder()
                .tasId(uniqueId)
                .hostLocator(teletypeEventDTO.getMessageCorrelationID())
                .carrierCode(teletypeEventDTO.getOrigin())
                .messageCorrelationId(String.valueOf(teletypeEventDTO.getMessageCorrelationID()))
                .sequenceNumber(Long.valueOf(String.valueOf(sequenceNumber++)))
                .createdTimestamp(LocalDateTime.now())
                .batchId(Integer.valueOf(10))
                .payload(messageXml)
                .payloadJson(messageJson)
                .build();
    }
}
