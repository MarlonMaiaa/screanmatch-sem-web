package br.com.alura.screnmach;

import br.com.alura.screnmach.model.DadosSeries;
import br.com.alura.screnmach.service.ConsumoApi;
import br.com.alura.screnmach.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScrenmachApplication implements CommandLineRunner {


	@Override
	public void run(String... args) throws Exception {

		ConsumoApi consumoApi = new ConsumoApi();

		var json = consumoApi.obterDados("https://www.omdbapi.com/?t=gilmore+girls&=1&apikey=b978461d");
		System.out.println(json);

//		json = consumoApi.obterDados("https://coffee.alexflipnote.dev/random.json?utm_source=chatgpt.com");
		System.out.println(json);
		ConverteDados conversor = new ConverteDados();
		DadosSeries dados = conversor.obterdados(json, DadosSeries.class);

		System.out.println(dados);
	}

	public static void main(String[] args) {
		SpringApplication.run(ScrenmachApplication.class, args);
	}

}
