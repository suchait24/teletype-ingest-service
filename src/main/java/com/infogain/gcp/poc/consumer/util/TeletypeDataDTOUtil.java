
package com.infogain.gcp.poc.consumer.util;
/*
import com.infogain.gcp.poc.consumer.dto.TeletypeDataDTO;
import com.infogain.gcp.poc.consumer.dto.TeletypeEventDTO;
import com.infogain.gcp.poc.consumer.entity.TeleTypeEntity;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

@Slf4j
public class TeletypeDataDTOUtil {

    public static String marshall(TeletypeDataDTO teletypeEventDTO) throws JAXBException {

        JAXBContext jaxbContext = JAXBContext.newInstance(TeletypeDataDTO.class);
        Marshaller marshaller = jaxbContext.createMarshaller();

        StringWriter stringWriter = new StringWriter();
        marshaller.marshal(teletypeEventDTO, stringWriter);

        String result = stringWriter.toString();
        //log.info("Teletype XML generated : {}", result);

        return result;
    }

    public static String getTeletypeDataDTOMessage(TeleTypeEntity teleTypeEntity) {

        TeletypeDataDTO teletypeDataDTO = new TeletypeDataDTO();
        teletypeDataDTO.setBatchId(teleTypeEntity.getBatchId());
        teletypeDataDTO.setCarrierCode(teleTypeEntity.getCarrierCode());
        teletypeDataDTO.setHostLocator(teleTypeEntity.getHostLocator());
        teletypeDataDTO.setMessageCorrelationId(teleTypeEntity.getMessageCorrelationId());
        teletypeDataDTO.setSequenceNumber(teleTypeEntity.getSequenceNumber());
        teletypeDataDTO.setTasId(teleTypeEntity.getTasId());
        teletypeDataDTO.setPayload(teleTypeEntity.getPayload());
        teletypeDataDTO.setCreatedTimestamp(teleTypeEntity.getCreatedTimestamp().toString());
        //teletypeDataDTO.setUpdatedTimestamp(teleTypeEntity.getUpdatedTimestamp().toString());

       return teletypeDataDTO.toString();

    }
}
*/