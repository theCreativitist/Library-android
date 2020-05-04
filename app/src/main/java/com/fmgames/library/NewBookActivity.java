package com.fmgames.library;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class NewBookActivity extends AppCompatActivity {

    private EditText bookNameEdit;
    private EditText currentPageEdit;
    private CheckBox haveReadCb;
    String bookNameStr;
    String currentPageStr;
    boolean haveReadBool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newbook);

        bookNameEdit = findViewById(R.id.bookNameEdit);
        currentPageEdit = findViewById(R.id.currentPageEdit);
        haveReadCb = findViewById(R.id.haveReadCb);
    }

    public void onSubmit(View v) {
        bookNameStr = bookNameEdit.getText().toString();
        currentPageStr = currentPageEdit.getText().toString();
        haveReadBool = haveReadCb.isChecked();

        SharedPreferences sharedP = getSharedPreferences("dataFile", MODE_PRIVATE);
        int indexFromSp = sharedP.getInt("index",0);
        SharedPreferences.Editor editor = sharedP.edit();
        editor.putString("Name"+indexFromSp, bookNameStr);
        editor.putString("Page"+indexFromSp, currentPageStr);
        editor.putInt("index",indexFromSp+1);
        //editor.putBoolean("HaveRead"+indexFromSp, haveReadBool);
        editor.commit();

        Intent i = new Intent(this, MainActivity.class);
        /*if (!bookNameStr.equals("")) {
            i.putExtra("Source", "NewBook");
            i.putExtra("Name", bookNameStr);
            i.putExtra("Page", currentPageStr);
            i.putExtra("HaveRead", haveReadBool);
        }*/
        startActivity(i);
    }
}
