package org.example.synthetichumancorestarter.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.synthetichumancorestarter.propertie.AuditProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class Audit {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private AuditProperties auditProperties;

    @Around("@annotation(org.example.synthetichumancorestarter.anotation.WeylandWatchingYou)")
    public Object audit(ProceedingJoinPoint joinPoint) throws Throwable {
        StringBuilder message = new StringBuilder();
        Object[] args = joinPoint.getArgs();

        message.append("Metods: " + joinPoint.getSignature().getName() + " Parametrs:");

        for (Object arg : args) {
            message.append(", " + arg);
        }

        Object result = joinPoint.proceed();
        message.append(" Result: " + result);

        if ("KAFKA".equalsIgnoreCase(auditProperties.getMode().toString())) {
            kafkaTemplate.send(auditProperties.getKafkaTopic(), message.toString());
        } else if ("LOG".equalsIgnoreCase(auditProperties.getMode().toString())) {
            log.info(message.toString());
        } else {
            log.warn("Введены некоректные настройки");
        }

        return result;
    }
}
