package com.example.bslapp;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.loopj.android.http.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.IntBuffer;
import java.util.Arrays;

import cz.msebera.android.httpclient.Header;

public class Camera extends AppCompatActivity implements View.OnClickListener {
    private Button btnTirarFoto;
    private ImageView imgCamera;
    protected static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 0;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_camera);
        btnTirarFoto = (Button) findViewById(R.id.btnTirarFoto);
        btnTirarFoto.setOnClickListener(this);
        imgCamera = (ImageView) findViewById(R.id.imgCamera);
    }

    @Override
    public void onClick(View v) {
        if(v ==  btnTirarFoto){
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        imgCamera.setImageBitmap(bitmap);
        Log.w("POST", "Começando..");
        SyncHttpClient client = new SyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("text", "some string");
        params.put("image", bitmap);
        Log.w("POST", "Parametros setados..");

        client.post("http://192.168.0.139:5000", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.w("POST", "onFailure: FALHOU");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.w("POST", "onSuccess: OK");
            }
        });
    }
}
