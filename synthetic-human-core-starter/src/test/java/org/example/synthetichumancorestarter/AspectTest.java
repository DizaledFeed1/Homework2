package org.example.synthetichumancorestarter;

import org.example.synthetichumancorestarter.service.CommandService;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
public class AspectTest {

    @Autowired
    private CommandService commandService;

    @Test
    void testAspectTriggered() {
        Map<String, Long> result = commandService.getDoneCommands();
        // Просто вызвать метод и проверить в логах, что аспект сработал.
        // Можно дополнительно проверить, что результат вызова не null
        System.out.println("Result: " + result);
    }

    @Test
    void testIsProxy() {
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(commandService);
        System.out.println("Real class: " + targetClass);
        System.out.println("Proxy class: " + commandService.getClass());

        assertNotEquals(targetClass, commandService.getClass(), "CommandService не является прокси!");
    }
}
