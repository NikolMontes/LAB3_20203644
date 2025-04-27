package com.example.teletrivia;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {
    private AutoCompleteTextView categoryDropdown, difficultyDropdown;
    private TextInputEditText optQuantity;
    private MaterialButton btnConection, btnStart;
    private boolean isConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EdgeToEdge.enable(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        categoryDropdown = findViewById(R.id.categoryDropdown);
        difficultyDropdown = findViewById(R.id.difficultyDropdown);
        optQuantity = findViewById(R.id.etQuantity);
        btnConection = findViewById(R.id.btnConnection);
        btnStart = findViewById(R.id.btnStart);

        btnStart.setEnabled(false);
        chooseDropdowns();

        btnConection.setOnClickListener(v -> {
            if (validarCampos()) {
                verificarConexion();
            }
        });

        btnStart.setOnClickListener(v -> {
            String categoria = categoryDropdown.getText().toString();
            String dificultad = difficultyDropdown.getText().toString();
            int cantidad = Integer.parseInt(optQuantity.getText().toString());

            Intent intent = new Intent(MainActivity.this, MainActivity2.class);
            intent.putExtra("categoria", categoria);
            intent.putExtra("dificultad", dificultad);
            intent.putExtra("cantidad", cantidad);
            startActivity(intent);
        });
    }

    private void chooseDropdowns() {
        // Opciones de categoría
        String[] categorias = {"Cultura General", "Libros", "Películas", "Música", "Computación", "Matemática", "Deportes", "Historia"};
        ArrayAdapter<String> adapterCategoria = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, categorias);
        categoryDropdown.setAdapter(adapterCategoria);

        // Opciones de dificultad
        String[] dificultades = {"fácil", "medio", "difícil"};
        ArrayAdapter<String> adapterDificultad = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, dificultades);
        difficultyDropdown.setAdapter(adapterDificultad);
    }

    private boolean validarCampos() {
        String categoria = categoryDropdown.getText().toString();
        String cantidadStr = optQuantity.getText().toString();
        String dificultad = difficultyDropdown.getText().toString();

        if (categoria.isEmpty() || cantidadStr.isEmpty() || dificultad.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            int cantidad = Integer.parseInt(cantidadStr);
            if (cantidad <= 0) {
                Toast.makeText(this, "La cantidad debe ser mayor que 0.", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Ingrese una cantidad válida.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void verificarConexion() {
        new Thread(() -> {
            isConnected = checkInternetConnection();
            runOnUiThread(() -> {
                if (isConnected) {
                    Toast.makeText(MainActivity.this, "¡Conexión exitosa!", Toast.LENGTH_SHORT).show();
                    btnStart.setEnabled(true); // Habilitar botón "Comenzar"
                } else {
                    Toast.makeText(MainActivity.this, "No tienes conexión a Internet.", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    private boolean checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }
}