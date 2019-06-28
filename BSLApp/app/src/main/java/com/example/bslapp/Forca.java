package com.example.bslapp;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.file.Files;
import java.util.LinkedList;
import java.util.Random;

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

            if (strikeCounter < selectedWordCapitalized.length()) {
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
            Intent i = new Intent(this, Camera.class);
            this.startActivity(i);
        }
    }
}
