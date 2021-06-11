package com.infogain.gcp.poc.consumer.component;
/*

import com.infogain.gcp.poc.consumer.entity.TeleTypeEntity;
import com.infogain.gcp.poc.consumer.util.TeletypeDataDTOUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class TeletypePublisher {

    private final BatchPublisher batchPublisher;

    public void processPublish(List<TeleTypeEntity> teleTypeEntityList) throws InterruptedException, ExecutionException, JAXBException, IOException {

        List<String> teletypeDataDTOMessagesList = teleTypeEntityList.stream()
                .map(element -> TeletypeDataDTOUtil.getTeletypeDataDTOMessage(element))
                .collect(Collectors.toList());

        batchPublisher.publishMessage(teletypeDataDTOMessagesList);
        log.info("Messages have been successfully published.");
    }
}
*/