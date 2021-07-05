package com.infogain.gcp.poc.consumer.pubsub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gcp.pubsub.support.converter.PubSubMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class PubSubConfig2 {

    @Bean
    public PubSubMessageConverter pubSubMessageConverter() {
        return new XmlPubSubMessageConverter();
    }

}
