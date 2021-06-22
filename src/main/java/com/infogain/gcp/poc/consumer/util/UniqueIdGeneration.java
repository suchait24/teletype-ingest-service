package com.infogain.gcp.poc.consumer.util;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UniqueIdGeneration {

    private static final Integer UNIQUE_ID_SIZE = 8;
    private static final char[] alphabets = ("0123456789ghijklmnopqrstuvwxyzGHIJKLMNOPQRSTUVWXYZ").toCharArray();

    public static String getUniqueId() {

        String uniqueId = NanoIdUtils.randomNanoId(NanoIdUtils.DEFAULT_NUMBER_GENERATOR,alphabets,UNIQUE_ID_SIZE);
        //log.info("Unique id generated : {}", uniqueId);
        return uniqueId;
    }

}
