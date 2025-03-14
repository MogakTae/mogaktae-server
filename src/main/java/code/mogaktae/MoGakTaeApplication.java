package code.mogaktae;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableCaching
@EnableJpaAuditing
@SpringBootApplication
public class MoGakTaeApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoGakTaeApplication.class, args);
	}

}
