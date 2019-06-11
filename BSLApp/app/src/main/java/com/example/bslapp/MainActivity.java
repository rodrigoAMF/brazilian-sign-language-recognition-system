package com.example.bslapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static CallbackManager callbackManager;
    private LoginButton loginButton;
    com.facebook.AccessToken userToken;
    private String name;
    private GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount account;
    private SignInButton signInButton;
    private int codigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        //login com google
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        account = GoogleSignIn.getLastSignedInAccount(this);
        if (account == null) {
            Log.d("TAG", "não fez login ainda");
        } else {
            Log.d("TAG", "já fez login");
        }

        signInButton = (SignInButton) findViewById(R.id.buttonSignin);
        signInButton.setOnClickListener(this);


        //login com facebook
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
        Intent intent = new Intent(this, Jogar.class);
        startActivity(intent);
    }


    @Override
    public void onClick(View v) {
        if (v == signInButton) {
            Toast.makeText(this, "Sign in", Toast.LENGTH_SHORT).show();
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, codigo);
        }
    }

/*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == codigo) {
            Task<GoogleSignInAccount> task  = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }*/

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            final GoogleSignInAccount accout = completedTask.getResult(ApiException.class);
            Log.d("TAG", "Deu certo!");
            Log.d("TAG", account.getDisplayName());Log.d("TAG", "Deu certo!2");
            Log.d("TAG", account.getEmail());
            Log.d("TAG", account.getFamilyName());

        } catch (ApiException e) {
            Log.d("TAG", "Deu errado");

            e.printStackTrace();
        }
    }
}
