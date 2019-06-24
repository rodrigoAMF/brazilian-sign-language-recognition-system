package com.example.bslapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.Random;

public class Forca extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout layout;
    private LinkedList<LinkedList<String>> wordAndTip;
    private LinkedList<String> dataList;
    private TextView tvDica;
    private Button btnInsere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forca);

        tvDica = (TextView) findViewById(R.id.tvDica);
        btnInsere = (Button) findViewById(R.id.btnInsere);

        wordAndTip = new LinkedList<>();
        dataList = new LinkedList<>();

        dataList.add("Maça");
        dataList.add("Dica: é um alimento rico em fibras.");
        wordAndTip.add(dataList);

        dataList = new LinkedList<>();

        dataList.add("Laranja");
        dataList.add("Dica: possui vitamina C.");
        wordAndTip.add(dataList);

        LinkedList<String> selectedWordAndTip = pickARandomWord(wordAndTip);

        String word = selectedWordAndTip.get(0);

        layout = (LinearLayout) findViewById(R.id.linearLayout);

        LinkedList<EditText> editTextList = new LinkedList<>();

        fillLinearLayout(word, editTextList, layout);

        showLetterInLinearLayout(2, word, editTextList);
        showLetterInLinearLayout(0, word, editTextList);

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
    }

    private LinkedList<String> pickARandomWord (LinkedList<LinkedList<String>> wordAndTip) {

        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(wordAndTip.size());

        return wordAndTip.get(randomInt);
    }


    @Override
    public void onClick(View v) {
        if (v == btnInsere) {
            //abre camera e pega libras
        }
    }
}
