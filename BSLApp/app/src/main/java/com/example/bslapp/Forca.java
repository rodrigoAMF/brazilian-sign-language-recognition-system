package com.example.bslapp;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Random;

import cz.msebera.android.httpclient.Header;

public class Forca extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout layout;
    private LinkedList<LinkedList<String>> wordAndTip;
    private LinkedList<String> dataList;
    private LinkedList<String> insertedLetters;
    private TextView tvDica;
    private EditText etInsert;
    private Button btnInsert, btnCamera;
    private String selectedWord;
    private ImageView imgHead;
    private LinkedList<EditText> editTextList = new LinkedList<>();
    private int errorCounter, strikeCounter;
    private char[] classes = new char[3];
    protected static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 0;
    private Uri imageUri;
    String currentPhotoPath;
    private Dialog myDialog;
    private LinearLayout idLetra1;
    private LinearLayout idLetra2;
    private LinearLayout idLetra3;
    private TextView idLetra1Txt;
    private TextView idLetra2Txt;
    private TextView idLetra3Txt;
    private String[] stringVet = new String[3];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forca);

        tvDica = (TextView) findViewById(R.id.tvDica);

        etInsert = (EditText) findViewById(R.id.etInsert);
        btnInsert = (Button) findViewById(R.id.btnInsere);
        btnCamera = (Button) findViewById(R.id.btnCamera);
        imgHead = (ImageView) findViewById(R.id.imageView2);
        btnInsert.setOnClickListener(this);
        btnCamera.setOnClickListener(this);
        myDialog = new Dialog(this);
        errorCounter = 0;
        strikeCounter = 0;

        wordAndTip = new LinkedList<>();
        dataList = new LinkedList<>();
        insertedLetters = new LinkedList<>();

        dataList.add("Maça");
        dataList.add("Dica: é um alimento rico em fibras.");
        wordAndTip.add(dataList);

        dataList = new LinkedList<>();

        dataList.add("Laranja");
        dataList.add("Dica: possui vitamina C.");
        wordAndTip.add(dataList);

        LinkedList<String> selectedWordAndTip = pickARandomWord(wordAndTip);

        selectedWord = selectedWordAndTip.get(0);

        layout = (LinearLayout) findViewById(R.id.linearLayout);

        fillLinearLayout(selectedWord, editTextList, layout);

        tvDica.setText(selectedWordAndTip.get(1));

    }

    private void fillLinearLayout (String word, LinkedList<EditText> editTextList, LinearLayout layout) {
        for (int i = 0; i < word.length(); i++) {
            EditText aux = new EditText(this);
            editTextList.add(aux);
            aux.setInputType(InputType.TYPE_NULL);
            layout.addView(editTextList.get(i));
        }
    }

    private void showLetterInLinearLayout (int letterPosition, String word, LinkedList<EditText> editTextList) {
        editTextList.get(letterPosition).setText(Character.toString(word.charAt(letterPosition)));
        insertedLetters.add(Character.toString(word.charAt(letterPosition)).toUpperCase());
    }

    private LinkedList<String> pickARandomWord (LinkedList<LinkedList<String>> wordAndTip) {

        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(wordAndTip.size());

        return wordAndTip.get(randomInt);
    }

    private void scorePoint (String selectedWordCapitalized, String etInsertStringCapitalized,  LinkedList<Integer> repeatedLetter) {
        boolean alreadySetted = false;


        for (int i=0; i < insertedLetters.size(); i++) {
            if (insertedLetters.get(i).equals(etInsertStringCapitalized)) {
                alreadySetted = true;
            }
        }

        for (int i = 0; i < selectedWordCapitalized.length(); i++) {
            if (selectedWordCapitalized.charAt(i) == etInsertStringCapitalized.toCharArray()[0]) {
                repeatedLetter.add(i);
            }
        }

        if (!alreadySetted) {

            for (int i = 0; i < repeatedLetter.size(); i++) {
                showLetterInLinearLayout(repeatedLetter.get(i), selectedWord, editTextList);
                strikeCounter++;

            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnInsert) {
            //abre camera e pega libras

            LinkedList<Integer> repeatedLetter = new LinkedList<>();

            String selectedWordCapitalized = selectedWord.toUpperCase();

            String etInsertStringCapitalized = etInsert.getText().toString().toUpperCase();

            if (strikeCounter < selectedWordCapitalized.length()-1) {
                if ((selectedWordCapitalized.contains(etInsertStringCapitalized)) && (!etInsertStringCapitalized.isEmpty()) &&
                        (errorCounter < 5)) {
                    scorePoint(selectedWordCapitalized, etInsertStringCapitalized, repeatedLetter);
                } else {
                    errorCounter++;

                    Toast.makeText(getApplicationContext(), "Não existe essa letra.", Toast.LENGTH_SHORT).show();

                    switch (errorCounter) {
                        case 1:
                            imgHead.setImageResource(R.drawable.forcacabeca);
                            break;
                        case 2:
                            imgHead.setImageResource(R.drawable.forcatronco);
                            break;
                        case 3:
                            imgHead.setImageResource(R.drawable.forcabracos);
                            break;
                        case 4:
                            imgHead.setImageResource(R.drawable.forcacompleta);
                            break;
                        default:
                            Toast.makeText(getApplicationContext(), "Fim de jogo. Você perdeu.", Toast.LENGTH_LONG).show();
                            break;
                    }
                }
            } else {
                Toast.makeText(getApplicationContext(), "Fim de jogo. Você ganhou.", Toast.LENGTH_LONG).show();
            }

        }
        else if(v == btnCamera){
            //dispatchTakePictureIntent();
            String[] str = new String[3];

            str[0] = "A";
            str[1] = "E";
            str[2] = "T";

            showPopup(str);

        } else if (v == idLetra1) {
            etInsert.setText(stringVet[0]);
            myDialog.dismiss();
        }else if (v == idLetra2) {
            etInsert.setText(stringVet[1]);
            myDialog.dismiss();
        }else if (v == idLetra3) {
            etInsert.setText(stringVet[2]);
            myDialog.dismiss();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                System.out.println("Vish deu ruim mano");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                imageUri = photoURI;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    public Bitmap grabImage()
    {
        this.getContentResolver().notifyChange(imageUri, null);
        ContentResolver cr = this.getContentResolver();
        Bitmap bitmap = null;
        try
        {
            bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, imageUri);
            //imageView.setImageBitmap(bitmap);
        }
        catch (Exception e)
        {
            Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
            Log.d("Erro", "Failed to load", e);
        }
        return bitmap;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        System.out.println("img uri "+imageUri);

        Bitmap bitmap = grabImage();
        bitmap = RotateBitmap(bitmap, 90.0f);

        Log.w("POST", "Começando..");
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream); //compress to which format you want.
        byte [] byte_arr = stream.toByteArray();
        String image_str = Base64.encodeToString(byte_arr, Base64.DEFAULT);
        params.put("image", image_str);
        Log.w("POST", "Parametros setados..");

        client.post("http://192.168.43.110:5000", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                String resposta = new String(response);
                Log.w("POST", "onSuccess: OK");
                Log.w("POST", resposta);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                String response = new String(errorResponse);
                Log.w("POST", "onFailure: " + response);
                Log.w("POST", String.valueOf(statusCode));
            }
        });
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public void showPopup (String[] vet) {
        System.out.println("chamoou");
        myDialog.setContentView(R.layout.custompopup);

        stringVet = vet;

        idLetra1Txt = (TextView) myDialog.findViewById(R.id.idLetra1Texto);
        idLetra2Txt = (TextView) myDialog.findViewById(R.id.idLetra2Texto);
        idLetra3Txt = (TextView) myDialog.findViewById(R.id.idLetra3Texto);

        idLetra1 = (LinearLayout) myDialog.findViewById(R.id.idLetra1);
        idLetra2 = (LinearLayout) myDialog.findViewById(R.id.idLetra2);
        idLetra3 = (LinearLayout) myDialog.findViewById(R.id.idLetra3);

        idLetra1Txt.setText(vet[0]);
        idLetra2Txt.setText(vet[1]);
        idLetra3Txt.setText(vet[2]);

        idLetra1.setOnClickListener(this);
        idLetra2.setOnClickListener(this);
        idLetra3.setOnClickListener(this);

        myDialog.show();
    }
}
