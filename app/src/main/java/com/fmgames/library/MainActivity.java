package com.fmgames.library;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private CheckBox cb;
    private ListView list;
    private RadioGroup radio;

    String bookNameFromIntent;
    String currentPageFromIntent;
    boolean haveReadFromIntent;
    String sourceFromIntent;
    int indexFromIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cb = findViewById(R.id.cb);
        list = findViewById(R.id.list);
        radio = findViewById(R.id.radio);

        list.setVisibility(View.INVISIBLE);

        final ArrayList<String> items = new ArrayList<>();
        final ArrayList<Integer> readCounts = new ArrayList<>();


        items.add("A gentleman in Moscow");
        items.add("The 100 Dollars startup");
        items.add("Nicolas and Alexandra");
        items.add("A tale of two cities");
        items.add("How to be an Imperfectionist");

        readCounts.add(0);
        readCounts.add(30);
        readCounts.add(385);
        readCounts.add(0);
        readCounts.add(250);

        if (getIntent().getExtras() != null){
            bookNameFromIntent = getIntent().getExtras().getString("Name");
            currentPageFromIntent = getIntent().getExtras().getString("Page");
            haveReadFromIntent = getIntent().getExtras().getBoolean("HaveRead");
            sourceFromIntent = getIntent().getExtras().getString("Source");
            indexFromIntent = getIntent().getExtras().getInt("Index");

            // System.out.println("XXX__ indexFromIndent in mainactivity got = "+ indexFromIntent);

            if (sourceFromIntent.equals("NewBook")){
                items.add(bookNameFromIntent);
                if (currentPageFromIntent.equals(""))
                    currentPageFromIntent = "0";
                readCounts.add(Integer.parseInt(currentPageFromIntent));
            }
            else if (sourceFromIntent.equals("EditBook")){
                items.set(indexFromIntent, bookNameFromIntent);
                if (currentPageFromIntent.equals(""))
                    currentPageFromIntent = "0";
                readCounts.set(indexFromIntent, Integer.parseInt(currentPageFromIntent));
            }
        }

        ArrayAdapter<String> adapterArray = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                items
        );

        list.setAdapter(adapterArray);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Toast.makeText(MainActivity.this, String.valueOf(readCounts.get(i)), Toast.LENGTH_SHORT).show();
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //readCounts.set(i, readCounts.get(i) + 1);
                Toast.makeText(MainActivity.this, "Increased by one", Toast.LENGTH_SHORT).show();
                Intent ebIntent = new Intent(MainActivity.this, EditBookActivity.class);
                ebIntent.putExtra("Name", items.get(i));
                ebIntent.putExtra("Page", readCounts.get(i).toString());
                ebIntent.putExtra("Index", i);
                //ebIntent.putExtra("HaveRead", haveReadBool);
                startActivity(ebIntent);
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflator = getMenuInflater();
        inflator.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.aboutMenu:
                toaster("This program is made by Amirmahdi");
                return true;
            case R.id.newBookMenu:
                //toaster("You are about to make a new book");
                Intent i = new Intent(this, NewBookActivity.class);
                startActivity(i);
                return true;
            default:
                toaster("Not Defiened");
                return super.onOptionsItemSelected(item);
        }
    }

    public void btnClick(View v) {
        if(cb.isChecked()){
            //toaster("You are not a bot!");
            int radioCheckedItem = radio.getCheckedRadioButtonId();
            switch (radioCheckedItem){
                case R.id.radioBtnDefault:
                    list.setBackgroundColor(Color.TRANSPARENT);
                    break;
                case R.id.radioBtnRed:
                    list.setBackgroundColor(getResources().getColor(R.color.red));
                    break;
                case R.id.radioBtnBlue:
                    list.setBackgroundColor(getResources().getColor(R.color.blue));
                    break;
            }
            list.setVisibility(View.VISIBLE);
        }
        else{
            toaster("Not accessable for bots like you!");
        }
    }

    public void toaster(String txt){ Toast.makeText(this, txt, Toast.LENGTH_SHORT).show(); }

    //TODO make a edit book activity
    //todo check material.io
}
