package com.fmgames.library;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class EditBookActivity extends AppCompatActivity {
    private static final String TAG = "EditBookActivity";

    private String nameFromIntent;
    private String pageFromIntent;
    int indexFromIntent;
    private CheckBox cb;

    private EditText nameEdit;
    private EditText pageEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editbook);

        nameFromIntent = getIntent().getExtras().getString("Name");
        pageFromIntent = getIntent().getExtras().getString("Page");
        indexFromIntent = getIntent().getExtras().getInt("Index");

        nameEdit = findViewById(R.id.bookNameEdit);
        pageEdit = findViewById(R.id.currentPageEdit);
        cb = findViewById(R.id.haveReadCb);

        nameEdit.setText(nameFromIntent);
        pageEdit.setText(pageFromIntent);

    }

    public void onSubmit(View v){
        String bookNameStr = nameEdit.getText().toString();
        String currentPageStr = pageEdit.getText().toString();
        boolean haveReadBool = cb.isChecked();
        Log.d(TAG, "XXXXXXX___ indexfromindent in onSubmit of editbookactivity = "+indexFromIntent);
        int indexInt = indexFromIntent;

        SharedPreferences sharedP = getSharedPreferences("dataFile", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedP.edit();
        editor.putString("Name"+indexInt, bookNameStr);
        editor.putString("Page"+indexInt, currentPageStr);
        //editor.putBoolean("HaveRead"+indexFromSp, haveReadBool);
        editor.commit();

        Intent i = new Intent(this, MainActivity.class);
        /*if (!bookNameStr.equals("")) {
            i.putExtra("Source", "EditBook");
            i.putExtra("Name", bookNameStr);
            i.putExtra("Page", currentPageStr);
            i.putExtra("HaveRead", haveReadBool);
            i.putExtra("Index", indexFromIntent);
            //System.out.println("XXX__ indexFromIndent in editbook passed = "+ indexFromIntent);
        }*/
        startActivity(i);
    }

}
