package id.segari.ortools;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SegariOrtoolServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SegariOrtoolServiceApplication.class, args);
	}

	@Bean
	public Validator getValidator(){
		final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		return validatorFactory.getValidator();
	}

}
