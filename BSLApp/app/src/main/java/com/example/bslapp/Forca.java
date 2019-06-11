package com.example.bslapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.LinkedList;

public class Forca extends AppCompatActivity {

    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forca);

        String word = "batata";

        layout = (LinearLayout) findViewById(R.id.linearLayout);

        LinkedList<EditText> editTextList = new LinkedList<>();

        fillLinearLayout(word, editTextList, layout);

        showLetterInLinearLayout(2, word, editTextList);
        showLetterInLinearLayout(4, word, editTextList);
        showLetterInLinearLayout(0, word, editTextList);

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

}
