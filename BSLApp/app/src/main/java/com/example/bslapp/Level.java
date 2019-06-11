package com.example.bslapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Level extends AppCompatActivity implements View.OnClickListener {

    private Button btnFacil, btnMedio, btnDificil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        btnFacil = (Button) findViewById(R.id.btnFacil);
        btnFacil.setOnClickListener(this);

        btnMedio = (Button) findViewById(R.id.btnMedio);
        btnMedio.setOnClickListener(this);

        btnDificil = (Button) findViewById(R.id.btnDificil);
        btnDificil.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v == btnFacil) {
            Toast.makeText(this, "Fácil selecionado", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Forca.class);
            startActivity(intent);
        } else if (v == btnMedio) {
            Toast.makeText(this, "Médio selecionado", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Forca.class);
            startActivity(intent);
        } else if (v == btnDificil) {
            Toast.makeText(this, "Difícil selecionado", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Forca.class);
            startActivity(intent);
        }

    }
}
