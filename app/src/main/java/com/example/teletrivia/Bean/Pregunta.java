package com.example.teletrivia.Bean;

import java.util.ArrayList;

public class Pregunta {
    private String texto;
    private String respuestaCorrecta;
    private ArrayList<String> opciones;

    public Pregunta(String texto, String respuestaCorrecta, ArrayList<String> opciones) {
        this.texto = texto;
        this.respuestaCorrecta = respuestaCorrecta;
        this.opciones = opciones;
    }
    public String getTexto() {
        return texto;
    }

    public String getRespuestaCorrecta() {
        return respuestaCorrecta;
    }

    public ArrayList<String> getOpciones() {
        return opciones;
    }
}
