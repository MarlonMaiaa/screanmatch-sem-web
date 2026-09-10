package br.com.alura.screnmach.principal;

import br.com.alura.screnmach.model.DadosEpisodio;
import br.com.alura.screnmach.model.DadosSeries;
import br.com.alura.screnmach.model.DadosTemporada;
import br.com.alura.screnmach.model.Episodios;
import br.com.alura.screnmach.service.ConsumoApi;
import br.com.alura.screnmach.service.ConverteDados;
import tools.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
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

        Scanner leitura = new Scanner(System.in);
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

//        System.out.println("Digite um trecho do titulo do episodio");
//        var trechodotitulo = leitura.nextLine();
//
//        Optional<Episodios> episodioBuscado = episodios.stream()
//                .filter(e -> e.getTitulo().toUpperCase().contains(trechodotitulo.toUpperCase()))
//                .findFirst();
//        if (episodioBuscado.isPresent()) {
//            System.out.println("Episodio buscado com sucesso");
//            System.out.println("Temporada: " +episodioBuscado.get().getTemporada());
//        }
//        else {
//            System.out.println("Episodio nao encontrado");
//        }

//        System.out.println("A  parti de que ano deseja lista os episodios");
//        var ano = scanner.nextInt();
//        scanner.nextLine();
//
//        LocalDate dataBusca = LocalDate.of(ano, 1, 1);
//
//        DateTimeFormatter formatterBr = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        episodios.stream()
//                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
//                .forEach(e -> System.out.println(
//                        "Temporada:" + e.getTemporada() +
//                        "Episodio:" + e.getTitulo() +
//                        "Data de lancamento:" + e.getDataLancamento().format(formatterBr)
//                ));

        Map <Integer,Double> avaliacoesTemporada = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.groupingBy(Episodios::getTemporada,
                        Collectors.averagingDouble(Episodios::getAvaliacao)));
        System.out.println(avaliacoesTemporada);

        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.summarizingDouble(Episodios::getAvaliacao));
        System.out.println("Quantidade de temporadas: " + est.getCount());
        System.out.println("Top episodio:" + est.getMax());
        System.out.println("Pior episodio:" + est.getMin());
        System.out.println("Media:" + est.getAverage());

    }
}