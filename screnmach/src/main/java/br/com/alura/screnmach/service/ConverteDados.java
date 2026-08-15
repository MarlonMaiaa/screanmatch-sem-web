package br.com.alura.screnmach.service;

import tools.jackson.databind.ObjectMapper;

public class ConverteDados implements Iconvertedados {
    @Override
    public <T> T obterdados(String json, Class<T> classe) {
        return mapper.readValue(json, classe);
    }

    private ObjectMapper mapper = new ObjectMapper();

}
