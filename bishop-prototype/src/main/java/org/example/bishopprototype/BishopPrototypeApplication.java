package org.example.bishopprototype;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication(scanBasePackages = {
		"org.example.bishopprototype",
		"org.example.synthetichumancorestarter"
})
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class BishopPrototypeApplication {

	public static void main(String[] args) {
		SpringApplication.run(BishopPrototypeApplication.class, args);
	}
}
