package com.fmgames.library;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class EditBookActivity extends AppCompatActivity {
    private static final String TAG = "EditBookActivity";

    SharedPreferences sharedP;
    SharedPreferences.Editor editor;

    int indexFromIntent;

    private EditText nameEdit, pageEdit, authorEdit, totalPagesEdit, descEdit;
    private Spinner spinner;

    String bookNameStr, currentPageStr, authorStr, totalPagesStr, descStr, stateStr;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editbook);

        //nameFromIntent = getIntent().getExtras().getString("Name");
        //pageFromIntent = getIntent().getExtras().getString("Page");
        indexFromIntent = getIntent().getExtras().getInt("Index");

        nameEdit = findViewById(R.id.bookNameEdit);
        pageEdit = findViewById(R.id.currentPageEdit);
        authorEdit = findViewById(R.id.bookAuthorEdit);
        totalPagesEdit = findViewById(R.id.totalPagesEdit);
        descEdit = findViewById(R.id.bookDescEdit);
        spinner = findViewById(R.id.spinner);

        ArrayList<String> spinnerValues = new ArrayList<>();
        spinnerValues.add("Wanna read");
        spinnerValues.add("Currently reading");
        spinnerValues.add("Completed");
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerValues);
        spinner.setAdapter(spinnerAdapter);

        //nameEdit.setText(nameFromIntent);
        //pageEdit.setText(pageFromIntent);

        sharedP = getSharedPreferences("dataFile", MODE_PRIVATE);
        editor = sharedP.edit();

        nameEdit.setText(sharedP.getString("Name"+indexFromIntent, ""));
        pageEdit.setText(sharedP.getString("Page"+indexFromIntent, ""));
        authorEdit.setText(sharedP.getString("Author"+indexFromIntent, ""));
        totalPagesEdit.setText(sharedP.getString("Tpage"+indexFromIntent, ""));
        descEdit.setText(sharedP.getString("Desc"+indexFromIntent, ""));

        stateStr = sharedP.getString("State"+indexFromIntent,"");
        if (stateStr.equals("Wanna Read"))
            spinner.setSelection(0);
        else if (stateStr.equals("Currently reading"))
            spinner.setSelection(1);
        else if (stateStr.equals("Completed"))
            spinner.setSelection(2);
    }

    public void onSubmit(View v){
        bookNameStr = nameEdit.getText().toString();
        currentPageStr = pageEdit.getText().toString();
        authorStr = authorEdit.getText().toString();
        totalPagesStr = totalPagesEdit.getText().toString();
        descStr = descEdit.getText().toString();
        stateStr = spinner.getSelectedItem().toString();

        Log.d(TAG, "XXXXXXX___ indexfromindent in onSubmit of editbookactivity = "+indexFromIntent);
        int indexInt = indexFromIntent;


        editor.putString("Name"+indexInt, bookNameStr);
        editor.putString("Page"+indexInt, currentPageStr);
        editor.putString("Author"+indexInt, authorStr);
        editor.putString("Tpage"+indexInt, totalPagesStr);
        editor.putString("Desc"+indexInt, descStr);
        editor.putString("State"+indexInt, stateStr);
        //editor.putInt("SelfIndex"+indexInt, indexInt);
        editor.commit();

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void onDelete (View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Deleting a book");
        builder.setMessage("Are you sure you want to delete this book?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                editor.remove("Name"+indexFromIntent);
                editor.remove("Page"+indexFromIntent);
                editor.remove("Author"+indexFromIntent);
                editor.remove("Tpage"+indexFromIntent);
                editor.remove("Desc"+indexFromIntent);
                editor.remove("State"+indexFromIntent);
                editor.remove("SelfIndex"+indexFromIntent);
                editor.commit();
                Intent in = new Intent(EditBookActivity.this, MainActivity.class);
                startActivity(in);
            }
        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        builder.create().show();
    }

}
