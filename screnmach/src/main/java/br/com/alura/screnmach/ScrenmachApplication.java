package br.com.alura.screnmach;

import br.com.alura.screnmach.principal.Principal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScrenmachApplication implements CommandLineRunner {


	@Override
	public void run(String... args) throws Exception {

		Principal principal = new Principal();
		principal.exibirmenu();



	}

	public static void main(String[] args) {
		SpringApplication.run(ScrenmachApplication.class, args);
	}

}
