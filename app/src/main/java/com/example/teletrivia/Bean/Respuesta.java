package com.example.teletrivia.Bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Respuesta {
    @SerializedName("response_code")
    private int responseCode;
    private List<PreguntaApi> results;

    public int getResponseCode() {
        return responseCode;
    }

    public List<PreguntaApi> getResults() {
        return results;
    }
}
