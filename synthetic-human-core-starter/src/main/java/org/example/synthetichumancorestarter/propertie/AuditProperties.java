package org.example.synthetichumancorestarter.propertie;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.audit")
public class AuditProperties {
    private String mode = "LOG";
    private String kafkaTopic = "audit-topic";
}
