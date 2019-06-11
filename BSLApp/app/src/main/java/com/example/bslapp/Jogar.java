package com.example.bslapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Jogar extends AppCompatActivity implements View.OnClickListener {

    private String userName;

    private Button btnAprender, btnJogar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogar);

        SharedPreferences preferences =
                getSharedPreferences("user_data", MODE_PRIVATE);

        userName =  preferences.getString("name", null);

        Toast.makeText(getApplicationContext(), "Usuário logado: "+userName, Toast.LENGTH_LONG).show();

        btnAprender = (Button) findViewById(R.id.btnAprender);
        btnAprender.setOnClickListener(this);

        btnJogar = (Button) findViewById(R.id.btnJogar);
        btnJogar.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == btnAprender) {
            Toast.makeText(this, "Aprender selecionado", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Aprender.class);
            startActivity(intent);
        } else if (v == btnJogar) {
            Toast.makeText(this, "Jogar selecionado", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Level.class);
            startActivity(intent);
        }
    }
}
