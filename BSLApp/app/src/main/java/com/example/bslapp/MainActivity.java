package com.example.bslapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static CallbackManager callbackManager;
    private LoginButton loginButton;
    com.facebook.AccessToken userToken;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        loginButton = (LoginButton) findViewById(R.id.fb_login_button);
        callbackManager = CallbackManager.Factory.create();

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                userToken = loginResult.getAccessToken();

                /*Faço a requisição para a Graph do facebook para coletar o nome do usuário logado pelo facebook */
                GraphRequest request = GraphRequest.newMeRequest(
                        userToken,
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                try {
                                    Toast.makeText(getApplicationContext(), "Login executado com sucesso.", Toast.LENGTH_LONG).show();
                                    name = object.getString("name");

                                    SharedPreferences preferences =
                                            getSharedPreferences("user_data", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("name", name);
                                    editor.commit();

                                    openLevel();
                                } catch (JSONException e) {
                                    Toast.makeText(getApplicationContext(), "Exception.", Toast.LENGTH_LONG).show();

                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("nome", name);
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Login cancelado", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "Houve um erro ao realizar login", Toast.LENGTH_LONG).show();
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void openLevel() {
        //Cria um intent da segunda tela
        Intent intent = new Intent(this, Level.class);
        startActivity(intent);
    }


}
