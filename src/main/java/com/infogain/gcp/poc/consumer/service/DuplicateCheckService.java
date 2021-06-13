package com.infogain.gcp.poc.consumer.service;

import com.infogain.gcp.poc.consumer.repository.TASRepository;
import com.infogain.gcp.poc.consumer.util.UniqueIdGeneration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class DuplicateCheckService {

    private final TASRepository tasRepository;

    public String getTasUniqueId() {

        String uniqueId = UniqueIdGeneration.getUniqueId();

        boolean flag = tasRepository.existsById(uniqueId);

        while(flag) {
            log.info("Id already exist in TAS table, generating again.");
            uniqueId = UniqueIdGeneration.getUniqueId();
            flag = tasRepository.existsById(uniqueId);
        }

        return uniqueId;
    }
}
