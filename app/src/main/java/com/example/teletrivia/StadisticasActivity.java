package com.example.teletrivia;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class StadisticasActivity extends AppCompatActivity {
    TextView tvCorrectas, tvIncorrectas, tvNoRespondidas;
    Button btnVolverJugar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_stadisticas);
        tvCorrectas = findViewById(R.id.tvCorrectas);
        tvIncorrectas = findViewById(R.id.tvIncorrectas);
        tvNoRespondidas = findViewById(R.id.tvNoRespondidas);
        btnVolverJugar = findViewById(R.id.btnVolverJugar);

        // Recibe datos
        int correctas = getIntent().getIntExtra("correctas", 0);
        int incorrectas = getIntent().getIntExtra("incorrectas", 0);
        int noRespondidas = getIntent().getIntExtra("noRespondidas", 0);

        // Muestra resultados
        tvCorrectas.setText("Correctas: " + correctas);
        tvIncorrectas.setText("Incorrectas: " + incorrectas);
        tvNoRespondidas.setText("No Respondidas: " + noRespondidas);

        btnVolverJugar.setOnClickListener(v -> {
            Intent intent = new Intent(StadisticasActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}