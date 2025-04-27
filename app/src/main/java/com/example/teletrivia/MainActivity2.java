package com.example.teletrivia;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.teletrivia.Bean.ApiService;
import com.example.teletrivia.Bean.Pregunta;
import com.example.teletrivia.Bean.PreguntaApi;
import com.example.teletrivia.Bean.Respuesta;
import com.example.teletrivia.Bean.RetrofitCliente;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity2 extends AppCompatActivity {
    TextView tvTitulot, tvCategoriat, tvTimer, tvNumeroPregunta, tvPregunta;
    RadioGroup rgOpciones;
    RadioButton rbOpcion1, rbOpcion2, rbOpcion3, rbOpcion4;
    Button btnSiguiente;

    ArrayList<Pregunta> preguntas = new ArrayList<>();
    int preguntaActual = 0;
    int correctas = 0, incorrectas = 0, noRespondidas = 0;
    int tiempoRestante; // en segundos
    boolean juegoTerminado = false;

    Handler handler = new Handler(Looper.getMainLooper());
    Runnable contadorRunnable;

    String categoriaElegida;
    int cantidad;
    String dificultadElegida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);

        tvTitulot = findViewById(R.id.tvTitulot);
        tvCategoriat = findViewById(R.id.tvCategoriat);
        tvTimer = findViewById(R.id.tvTimer);
        tvNumeroPregunta = findViewById(R.id.tvNumeroPregunta);
        tvPregunta = findViewById(R.id.tvPregunta);
        rgOpciones = findViewById(R.id.rgOpciones);
        rbOpcion1 = findViewById(R.id.rbOpcion1);
        rbOpcion2 = findViewById(R.id.rbOpcion2);
        rbOpcion3 = findViewById(R.id.rbOpcion3);
        rbOpcion4 = findViewById(R.id.rbOpcion4);
        btnSiguiente = findViewById(R.id.btnSiguiente);

        // Recibir datos
        categoriaElegida = getIntent().getStringExtra("categoria");
        dificultadElegida = getIntent().getStringExtra("dificultad");
        cantidad = getIntent().getIntExtra("cantidad", 5);

        // Mostrar categoría
        tvCategoriat.setText(categoriaElegida);

        // Calcular tiempo total
        int segundosPorPregunta = getSegundosPorPregunta(dificultadElegida);
        tiempoRestante = cantidad * segundosPorPregunta;

        // Iniciar contador
        iniciarContador();
        // Obtener preguntas
        obtenerPreguntas();

        // Botón siguiente
        btnSiguiente.setOnClickListener(v -> {
            evaluarRespuesta();
            siguientePregunta();
        });

    }


    private void iniciarContador() {
        contadorRunnable = new Runnable() {
            @Override
            public void run() {
                if (tiempoRestante > 0 && !juegoTerminado) {
                    tiempoRestante--;
                    tvTimer.setText(tiempoRestante + "s");
                    Log.d("JUGANDO", "Tiempo restante: " + tiempoRestante + " segundos");
                    handler.postDelayed(this, 1000);
                } else {
                    terminarJuego();
                }
            }
        };
        handler.post(contadorRunnable);
    }

    private void obtenerPreguntas() {
        ApiService apiService = RetrofitCliente.getClient().create(ApiService.class);

        Call<Respuesta> call = apiService.obtenerPreguntas(cantidad, mapDificultad(dificultadElegida), "multiple");

        call.enqueue(new Callback<Respuesta>() {
            @Override
            public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<PreguntaApi> lista = response.body().getResults();
                    Log.i("RECIBIENDO PREGUNTAS", "Preguntas recibidas: " + lista.size());
                    for (PreguntaApi p : lista) {
                        ArrayList<String> opciones = new ArrayList<>();
                        opciones.add(p.getCorrect_answer());
                        opciones.addAll(p.getIncorrect_answers());
                        Collections.shuffle(opciones);
                        preguntas.add(new Pregunta(p.getQuestion(), p.getCorrect_answer(), opciones));
                    }
                    mostrarPreguntaActual();
                }
            }

            @Override
            public void onFailure(Call<Respuesta> call, Throwable t) {
                Toast.makeText(MainActivity2.this, "Error al obtener preguntas", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int getSegundosPorPregunta(String dificultad) {
        switch (dificultad.toLowerCase()) {
            case "fácil": return 5;
            case "medio": return 7;
            case "difícil": return 10;
            default: return 5;
        }
    }

    private void mostrarPreguntaActual() {
        if (preguntaActual < preguntas.size()) {
            Pregunta p = preguntas.get(preguntaActual);
            tvPregunta.setText(p.getTexto());
            tvNumeroPregunta.setText("Pregunta " + (preguntaActual + 1) + "/" + preguntas.size());

            rbOpcion1.setText(p.getOpciones().get(0));
            rbOpcion2.setText(p.getOpciones().get(1));
            rbOpcion3.setText(p.getOpciones().get(2));
            rbOpcion4.setText(p.getOpciones().get(3));
        }
    }

    private void evaluarRespuesta() {
        int idSeleccionado = rgOpciones.getCheckedRadioButtonId();
        if (idSeleccionado == -1) {
            noRespondidas++;
        } else {
            RadioButton seleccionada = findViewById(idSeleccionado);
            String respuestaDeUsuario = seleccionada.getText().toString();
            if (respuestaDeUsuario.equals(preguntas.get(preguntaActual).getRespuestaCorrecta())) {
                correctas++;
            } else {
                Log.d("RESPONDIENDO LAS PREGUNTAS", "Se seleccionó: " + respuestaDeUsuario);
                incorrectas++;
            }
        }
    }

    private void siguientePregunta() {
        preguntaActual++;
        if (preguntaActual < preguntas.size()) {
            rgOpciones.clearCheck();
            mostrarPreguntaActual();
        } else {
            terminarJuego();
        }
    }

    private void terminarJuego() {
        juegoTerminado = true;
        handler.removeCallbacks(contadorRunnable);

        Intent intent = new Intent(this, StadisticasActivity.class);
        intent.putExtra("correctas", correctas);
        intent.putExtra("incorrectas", incorrectas);
        intent.putExtra("noRespondidas", noRespondidas);
        startActivity(intent);
        finish();
    }

    private String mapDificultad(String dificultad) {
        switch (dificultad.toLowerCase()) {
            case "fácil": return "easy";
            case "medio": return "medium";
            case "difícil": return "hard";
            default: return "easy";
        }
    }

}