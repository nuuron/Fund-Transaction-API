package money.neowise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TransactionsApiApplication {

	public static void main(String[] args) {
		System.out.println("Hi there");
		SpringApplication.run(TransactionsApiApplication.class, args);
	}

}
