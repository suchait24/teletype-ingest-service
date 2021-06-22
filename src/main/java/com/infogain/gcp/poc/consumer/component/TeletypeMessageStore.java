package com.infogain.gcp.poc.consumer.component;

import com.infogain.gcp.poc.consumer.entity.TeleTypeEntity;
import com.infogain.gcp.poc.consumer.repository.TASRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
@RequiredArgsConstructor
public class TeletypeMessageStore {

    private final TASRepository tasRepository;
    private ExecutorService THREAD_POOL = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public void saveMessagesList(List<TeleTypeEntity> teleTypeEntityList) {
        //log.info("Saving all messages");

        teleTypeEntityList.stream().forEach(record -> CompletableFuture.supplyAsync(() -> tasRepository.save(record), THREAD_POOL));

        //log.info("All messages saved in database.");

    }
}
