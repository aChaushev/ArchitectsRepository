package bg.softuni.arch_repo.architects_offers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ArchitectsOffersApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArchitectsOffersApplication.class, args);
	}

}
