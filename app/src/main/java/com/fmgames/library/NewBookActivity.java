package com.fmgames.library;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Interpolator;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class NewBookActivity extends AppCompatActivity {

    private static final int GET_FROM_GALLERY = 99;
    private EditText bookNameEdit, currentPageEdit, authorEdit, descEdit, totalPagesEdit;
    private Spinner spinner;
    SimpleDraweeView imageButton;

    String bookNameStr;
    String currentPageStr;
    String authorStr, descStr, totalPagesStr;
    String coverUriStr;

    SharedPreferences sharedP;
    SharedPreferences.Editor editor;
    int indexFromSp, stateInt;

    boolean isCoverChanged = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fresco.initialize(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newbook);

        bookNameEdit = findViewById(R.id.bookNameEdit);
        currentPageEdit = findViewById(R.id.currentPageEdit);
        spinner = findViewById(R.id.spinner);
        authorEdit = findViewById(R.id.bookAuthorEdit);
        descEdit = findViewById(R.id.bookDescEdit);
        totalPagesEdit = findViewById(R.id.totalPagesEdit);
        imageButton = findViewById(R.id.imageButton);

        initSpinner();

        sharedP = getSharedPreferences("dataFile", MODE_PRIVATE);
        editor = sharedP.edit();
        indexFromSp = sharedP.getInt("index",0);
    }

    public void initSpinner() {
        ArrayList<String> spinnerValues = new ArrayList<>();
        spinnerValues.add(getString(R.string.wanna_read));
        spinnerValues.add(getString(R.string.reading));
        spinnerValues.add(getString(R.string.completed));

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerValues);
        spinner.setAdapter(spinnerAdapter);
    }

    public void onSubmit(View v) {
        bookNameStr = bookNameEdit.getText().toString();
        currentPageStr = currentPageEdit.getText().toString();
        authorStr = authorEdit.getText().toString();
        descStr = descEdit.getText().toString();
        totalPagesStr = totalPagesEdit.getText().toString();
        stateInt = spinner.getSelectedItemPosition();
        String stateStr = String.valueOf(stateInt);

        editor.putString("Name"+indexFromSp, bookNameStr);
        editor.putString("Page"+indexFromSp, currentPageStr);
        editor.putString("Author"+indexFromSp, authorStr);
        editor.putString("Desc"+indexFromSp, descStr);
        editor.putString("Tpage"+indexFromSp, totalPagesStr);
        editor.putString("State"+indexFromSp, stateStr);
        editor.putString("CoverUri"+indexFromSp, coverUriStr);
        editor.putInt("SelfIndex"+indexFromSp, indexFromSp);
        editor.putInt("index",indexFromSp+1);
        editor.commit();

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_FROM_GALLERY && resultCode == RESULT_OK && data != null){
            Uri imagePath = data.getData();
            final int takeFlags = data.getFlags()
                    & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            getContentResolver().takePersistableUriPermission(imagePath, takeFlags);
            coverUriStr = imagePath.toString();
            isCoverChanged = true;
            imageButton.setImageURI(coverUriStr);
        }
    }

    public void onAddCover(View v){
        Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        i.setType("image/*");
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        i.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        startActivityForResult(i, GET_FROM_GALLERY);
    }
}
