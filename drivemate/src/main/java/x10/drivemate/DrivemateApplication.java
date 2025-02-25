package x10.drivemate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DrivemateApplication {

	public static void main(String[] args) {
		SpringApplication.run(DrivemateApplication.class, args);
	}

}
