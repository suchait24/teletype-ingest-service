package com.infogain.gcp.poc.consumer.component;

import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.PullRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class PubSubConfig {

    @Value("${spring.cloud.gcp.project-id}")
    private String projectId;

    @Value("${app.subscription.id}")
    private String subscriptionId;

    @Value("${app.subscription.max.pull.count}")
    private Integer maxMessagePullCount;

    @Bean
    public PullRequest getPullRequest() {

        PullRequest.Builder builder =
                PullRequest.newBuilder()
                        .setSubscription(ProjectSubscriptionName.of(projectId, subscriptionId)
                                .toString()).setMaxMessages(maxMessagePullCount);

        return builder.build();
    }

}
