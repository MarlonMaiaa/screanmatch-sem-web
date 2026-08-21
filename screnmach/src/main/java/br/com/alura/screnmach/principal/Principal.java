package br.com.alura.screnmach.principal;

import br.com.alura.screnmach.model.DadosSeries;
import br.com.alura.screnmach.model.DadosTemporada;
import br.com.alura.screnmach.service.ConsumoApi;
import br.com.alura.screnmach.service.ConverteDados;
import tools.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {


    private Scanner scanner = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados converte = new ConverteDados();

    //Contantes
    private final  String URL = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=b978461d";

    public void exibirmenu () {

            System.out.println("Digite o nome da serie para busca");
            var nomeSerie = scanner.nextLine();

            var json = consumo.obterDados
                    (URL + nomeSerie.replace(" ", "+")+ API_KEY);

        DadosSeries dados = converte.obterdados(json, DadosSeries.class);

        List<DadosTemporada> temporadas = new ArrayList<>();
        for (int i = 1; i <= dados.totalTemporadas(); i++) {
            json = consumo.obterDados
                    (URL + nomeSerie.replace(" ", "+") + "&season=" + i + API_KEY);
            DadosTemporada dadosTemporada = converte.obterdados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }

        temporadas.forEach(System.out::println);

            System.out.println(json);

        System.out.println(dados);


    }
}
