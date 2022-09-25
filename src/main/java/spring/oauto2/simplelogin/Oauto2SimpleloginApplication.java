package spring.oauto2.simplelogin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Oauto2SimpleloginApplication {
	public static void main(String[] args) {
		SpringApplication.run(Oauto2SimpleloginApplication.class, args);
	}
}
