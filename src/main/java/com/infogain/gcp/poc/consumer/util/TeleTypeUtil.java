package com.infogain.gcp.poc.consumer.util;

import com.google.cloud.Timestamp;
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
import java.util.UUID;

@Slf4j
public class TeleTypeUtil {

    public static TeletypeEventDTO unmarshall(String message) throws JAXBException {

        JAXBContext jaxbContext = JAXBContext.newInstance(TeletypeEventDTO.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        TeletypeEventDTO teletypeEventDTO = (TeletypeEventDTO) unmarshaller.unmarshal(new StringReader(message));

        //log.info("Teletype dto generated : {}", teletypeEventDTO);

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

    public static TeleTypeEntity convert(TeletypeEventDTO teletypeEventDTO, String message, String messageJson) {

        return TeleTypeEntity.builder()
                .tasId(UUID.randomUUID().toString())
                .hostLocator(teletypeEventDTO.getHostLocator())
                .carrierCode(teletypeEventDTO.getCarrierCode())
                .messageCorrelationId(String.valueOf(teletypeEventDTO.getMessageCorrelationId()))
                .sequenceNumber(Long.valueOf(teletypeEventDTO.getSequenceNumber()))
                .createdTimestamp(Timestamp.now())
                .batchId(teletypeEventDTO.getBatchId())
                .payload(message)
                .payloadJson(messageJson)
                .build();
    }
}
