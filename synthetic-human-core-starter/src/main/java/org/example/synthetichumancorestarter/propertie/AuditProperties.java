package org.example.synthetichumancorestarter.propertie;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.audit")
public class AuditProperties {

    private AuditMode mode = AuditMode.LOG;
    private String kafkaTopic = "audit-topic";
}
