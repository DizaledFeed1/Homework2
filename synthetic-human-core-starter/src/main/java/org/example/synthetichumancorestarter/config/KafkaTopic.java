package org.example.synthetichumancorestarter.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.example.synthetichumancorestarter.propertie.AuditProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopic {
    @Autowired
    private AuditProperties auditProperties;

    @Bean
    public NewTopic topic1() {
        return TopicBuilder.name(auditProperties.getKafkaTopic()).build();
    }
}
