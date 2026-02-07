package ru.pp.gamma.overlord.kafka.topic;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import ru.pp.gamma.overlord.kafka.KafkaTopic;

@Configuration
public class PresentationSavedOrUpdatedKafkaTopicConfig {

    @Bean
    public NewTopic presentationSavedOrUpdated() {
        return TopicBuilder.name(KafkaTopic.PRESENTATION_SAVED_OR_UPDATED)
                .partitions(1)
                .replicas(1)
                .build();
    }

}
