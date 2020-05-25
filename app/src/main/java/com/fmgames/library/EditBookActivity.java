package com.fmgames.library;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class EditBookActivity extends AppCompatActivity {
    private static final String TAG = "EditBookActivity";
    private static final int GET_FROM_GALLERY = 88;

    SharedPreferences sharedP;
    SharedPreferences.Editor editor;

    int indexFromIntent;

    boolean isCoverChanged = false;

    private EditText nameEdit, pageEdit, authorEdit, totalPagesEdit, descEdit;
    private Spinner spinner;
    private SimpleDraweeView imageButton;

    String bookNameStr, currentPageStr, authorStr, totalPagesStr, descStr;
    String coverUriStr;
    int stateInt;

    String bookName;
    String bookPage;
    String bookAuthor;
    String bookTpage;
    String bookDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        Fresco.initialize(this);
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
        imageButton = findViewById(R.id.imageButton);

        ArrayList<String> spinnerValues = new ArrayList<>();
        spinnerValues.add(getString(R.string.wanna_read));
        spinnerValues.add(getString(R.string.reading));
        spinnerValues.add(getString(R.string.completed));
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerValues);
        spinner.setAdapter(spinnerAdapter);

        //nameEdit.setText(nameFromIntent);
        //pageEdit.setText(pageFromIntent);

        sharedP = getSharedPreferences("dataFile", MODE_PRIVATE);
        editor = sharedP.edit();

        bookName = sharedP.getString("Name"+indexFromIntent, "");
        bookPage = sharedP.getString("Page"+indexFromIntent, "");
        bookAuthor = sharedP.getString("Author"+indexFromIntent, "");
        bookTpage = sharedP.getString("Tpage"+indexFromIntent, "");
        bookDesc = sharedP.getString("Desc"+indexFromIntent, "");

        nameEdit.setText(bookName);
        pageEdit.setText(bookPage);
        authorEdit.setText(bookAuthor);
        totalPagesEdit.setText(bookTpage);
        descEdit.setText(bookDesc);

        stateInt = Integer.parseInt(sharedP.getString("State"+indexFromIntent,""));
        spinner.setSelection(stateInt);

        Uri coverUri = Uri.parse(sharedP.getString("CoverUri"+indexFromIntent, ""));
        //imageButton.setImageURI(coverUri);
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(coverUri)
                .setResizeOptions(new ResizeOptions(250, 375))
                .build();
        imageButton.setController(
                Fresco.newDraweeControllerBuilder()
                        .setOldController(imageButton.getController())
                        .setImageRequest(request)
                        .build());
        /*InputStream is = null;
        try {
            is = getContentResolver().openInputStream(coverUri);
        } catch (FileNotFoundException | SecurityException e) {
            Log.d(TAG, "EXCEPTION: "+e);
        }
        if (is != null)
            imageButton.setImageURI(coverUri);*/
    }

    public void onSubmit(View v){
        bookNameStr = nameEdit.getText().toString();
        currentPageStr = pageEdit.getText().toString();
        authorStr = authorEdit.getText().toString();
        totalPagesStr = totalPagesEdit.getText().toString();
        descStr = descEdit.getText().toString();
        stateInt = spinner.getSelectedItemPosition();

        Log.d(TAG, "XXXXXXX___ indexfromindent in onSubmit of editbookactivity = "+indexFromIntent);
        int indexInt = indexFromIntent;


        editor.putString("Name"+indexInt, bookNameStr);
        editor.putString("Page"+indexInt, currentPageStr);
        editor.putString("Author"+indexInt, authorStr);
        editor.putString("Tpage"+indexInt, totalPagesStr);
        editor.putString("Desc"+indexInt, descStr);
        editor.putString("State"+indexInt, String.valueOf(stateInt));
        if (isCoverChanged)
            editor.putString("CoverUri"+indexInt, coverUriStr);
        editor.commit();

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void onDelete (View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(getString(R.string.delete_title));
        builder.setMessage(getString(R.string.delete_confirmation));
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                editor.remove("Name"+indexFromIntent);
                editor.remove("Page"+indexFromIntent);
                editor.remove("Author"+indexFromIntent);
                editor.remove("Tpage"+indexFromIntent);
                editor.remove("Desc"+indexFromIntent);
                editor.remove("State"+indexFromIntent);
                editor.remove("SelfIndex"+indexFromIntent);
                editor.remove("CoverUri"+indexFromIntent);
                editor.commit();
                Intent in = new Intent(EditBookActivity.this, MainActivity.class);
                startActivity(in);
            }
        })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        builder.create().show();
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

    public void onShare(View v){
        String shareText = getString(R.string.shareIntro) +"\n\n" +
                bookName +"\n" +
                getString(R.string.author) + ": " + bookAuthor +"\n" +
                bookDesc +"\n" +
                getString(R.string.pages_read) + " " + bookPage + " " + getString(R.string.of) + " " + bookTpage +"\n\n" +
                getString(R.string.promotion);
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        sendIntent.setType("text/plain");

        Intent shareSheetIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareSheetIntent);
    }

}
