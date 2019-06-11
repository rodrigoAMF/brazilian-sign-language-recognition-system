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
        Log.d("Debugging", "Before setting letter at position 2");
        showLetterInLinearLayout(2, word, editTextList);
        Log.d("Debugging", "After setting letter at position 4");
        showLetterInLinearLayout(4, word, editTextList);

    }

    private void fillLinearLayout (String word, LinkedList<EditText> editTextList, LinearLayout layout) {

        for (int i = 0; i < word.length(); i++) {
            EditText aux = new EditText(this);

            editTextList.add(aux);
            aux.setInputType(InputType.TYPE_NULL);

            layout.addView(editTextList.get(i));
        }
        Log.d("Debugging", "Filled fillLinearLayout");
    }

    private void showLetterInLinearLayout (int letterPosition, String word, LinkedList<EditText> editTextList) {
        Log.d("Debugging", "Inside showLetterInLinearLayout. Its size: "+editTextList.size());
        editTextList.get(2).setText("a");
        Log.d("Debugging", "Passou aqui: "+editTextList.size());

        editTextList.get(letterPosition).setText(word.charAt(letterPosition));
    }

}
