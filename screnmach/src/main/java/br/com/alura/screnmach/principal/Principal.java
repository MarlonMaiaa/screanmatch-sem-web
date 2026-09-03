package br.com.alura.screnmach.principal;

import br.com.alura.screnmach.model.DadosEpisodio;
import br.com.alura.screnmach.model.DadosSeries;
import br.com.alura.screnmach.model.DadosTemporada;
import br.com.alura.screnmach.model.Episodios;
import br.com.alura.screnmach.service.ConsumoApi;
import br.com.alura.screnmach.service.ConverteDados;
import tools.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {


    private Scanner scanner = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados converte = new ConverteDados();

    //Contantes
    private final String URL = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=b978461d";

    public void exibirmenu() {

        System.out.println("Digite o nome da serie para busca");
        var nomeSerie = scanner.nextLine();

        var json = consumo.obterDados
                (URL + nomeSerie.replace(" ", "+") + API_KEY);

        DadosSeries dados = converte.obterdados(json, DadosSeries.class);

        List<DadosTemporada> temporadas = new ArrayList<>();
        for (int i = 1; i <= dados.totalTemporadas(); i++) {
            json = consumo.obterDados
                    (URL + nomeSerie.replace(" ", "+") + "&season=" + i + API_KEY);
            DadosTemporada dadosTemporada = converte.obterdados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }


        temporadas.forEach(System.out::println);

        temporadas.forEach(t -> t.episodios().forEach
                (e-> System.out.println(e.titulo())));


        System.out.println("\nTop 5 melhores");
       List <DadosEpisodio> dadosEpisodios = temporadas.stream()
               .flatMap(t -> t.episodios().stream())
               .collect(Collectors.toList());

       dadosEpisodios.stream()
               .filter(e -> !e.avaliacaoEpisodio().equalsIgnoreCase("N/A"))
               .sorted(Comparator.comparing(DadosEpisodio::avaliacaoEpisodio).reversed())

               .limit(5)
               .forEach(System.out::println);

        List<Episodios> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodios(t.numero(), d))
                ).collect(Collectors.toList());

        episodios.forEach(System.out::println);



    }
}