package com.example.bslapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class Level extends AppCompatActivity {

    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        SharedPreferences preferences =
                getSharedPreferences("user_data", MODE_PRIVATE);

        userName =  preferences.getString("name", null);

        Toast.makeText(getApplicationContext(), "Usuário logado: "+userName, Toast.LENGTH_LONG).show();

    }
}
