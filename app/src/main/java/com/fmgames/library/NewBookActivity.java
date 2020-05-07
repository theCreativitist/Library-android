package com.fmgames.library;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class NewBookActivity extends AppCompatActivity {

    private EditText bookNameEdit, currentPageEdit, authorEdit, descEdit, totalPagesEdit;
    private Spinner spinner;


    String bookNameStr;
    String currentPageStr;
    String authorStr, descStr, totalPagesStr, stateStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newbook);

        bookNameEdit = findViewById(R.id.bookNameEdit);
        currentPageEdit = findViewById(R.id.currentPageEdit);
        spinner = findViewById(R.id.spinner);
        authorEdit = findViewById(R.id.bookAuthorEdit);
        descEdit = findViewById(R.id.bookDescEdit);
        totalPagesEdit = findViewById(R.id.totalPagesEdit);

        initSpinner();
    }

    public void initSpinner() {
        ArrayList<String> spinnerValues = new ArrayList<>();
        spinnerValues.add("Wanna read");
        spinnerValues.add("Currently reading");
        spinnerValues.add("Completed");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerValues);
        spinner.setAdapter(spinnerAdapter);
    }

    public void onSubmit(View v) {
        bookNameStr = bookNameEdit.getText().toString();
        currentPageStr = currentPageEdit.getText().toString();
        authorStr = authorEdit.getText().toString();
        descStr = descEdit.getText().toString();
        totalPagesStr = totalPagesEdit.getText().toString();
        stateStr = spinner.getSelectedItem().toString();

        SharedPreferences sharedP = getSharedPreferences("dataFile", MODE_PRIVATE);
        int indexFromSp = sharedP.getInt("index",0);
        SharedPreferences.Editor editor = sharedP.edit();
        editor.putString("Name"+indexFromSp, bookNameStr);
        editor.putString("Page"+indexFromSp, currentPageStr);
        editor.putString("Author"+indexFromSp, authorStr);
        editor.putString("Desc"+indexFromSp, descStr);
        editor.putString("Tpage"+indexFromSp, totalPagesStr);
        editor.putString("State"+indexFromSp, stateStr);
        editor.putInt("SelfIndex"+indexFromSp, indexFromSp);
        editor.putInt("index",indexFromSp+1);
        editor.commit();

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
