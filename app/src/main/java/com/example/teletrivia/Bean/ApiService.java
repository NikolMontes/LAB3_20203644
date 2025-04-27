package com.example.teletrivia.Bean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface  ApiService {
    @GET("api.php")
    abstract Call<Respuesta> obtenerPreguntas(
            @Query("amount") int amount,
            @Query("difficulty") String difficulty,
            @Query("type") String type
    );
}
