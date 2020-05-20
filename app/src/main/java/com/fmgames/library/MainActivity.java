package com.fmgames.library;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private FloatingActionButton fab;
    private RecyclerView recView;
    private BottomNavigationView bottomNav;
    private Spinner spinner, spinnerType;
    public TextView recViewPlaceholder;
    SearchView searchView;

    String bookNameFromIntent;
    String currentPageFromIntent;
    boolean haveReadFromIntent;
    String sourceFromIntent;
    int indexFromIntent;

    public ArrayList<Book> readingBooks = new ArrayList<>();
    public ArrayList<Book> wannaReadBooks = new ArrayList<>();
    public ArrayList<Book> completedBooks = new ArrayList<>();
    public ArrayList<Book> books = new ArrayList<>();

    public ArrayList<Book> sortBooks = new ArrayList<>();

    RecyclerViewAdapter recViewAdapter = new RecyclerViewAdapter(this);

    SharedPreferences sharedP;
    SharedPreferences.Editor editor;
    int spIndex;

    //TODO this shit is ashghal kari! do something
    boolean isSpinnerTypeFirstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recViewPlaceholder = findViewById(R.id.recyclerViewPlaceholder);
        initFab();
        initBottomNav();
        spinner = findViewById(R.id.sortBySpinner);
        spinnerType = findViewById(R.id.sortTypeSpinner);
        ArrayList<String> spinnerVals = new ArrayList<>();
        ArrayList<String> spinnerTypeVals = new ArrayList<>();
        spinnerVals.add(getString(R.string.title_book));
        spinnerVals.add(getString(R.string.progress));
        spinnerVals.add(getString(R.string.total_pages));
        spinnerVals.add(getString(R.string.current_page));
        spinnerTypeVals.add(getString(R.string.descending));
        spinnerTypeVals.add(getString(R.string.ascending));
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, spinnerVals);
        ArrayAdapter<String> spinnerTypeAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, spinnerTypeVals);
        spinner.setAdapter(spinnerAdapter);
        spinnerType.setAdapter(spinnerTypeAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0: //title
                        Comparator<Book> comparator = new Comparator<Book>() {
                            @Override
                            public int compare(Book book, Book t1) {
                                return compareFa(book.getTitle(), t1.getTitle());
                            }

                            public int compareFa (String s1, String s2){
                                Collator collator = Collator.getInstance(new Locale("fa","ir"));
                                collator.setStrength(Collator.SECONDARY);
                                ArrayList<String> arrayList = new ArrayList<>();
                                arrayList.add(s1);
                                arrayList.add(s2);
                                //Log.d(TAG, "_____///BEFORE: ("+arrayList.get(0)+") , ("+arrayList.get(1)+")______");
                                Collections.sort(arrayList, collator);
                                //Log.d(TAG, "_____AFTER: ("+arrayList.get(0)+") , ("+arrayList.get(1)+")______");
                                if (arrayList.get(0).equals(s1)){
                                    //Log.d(TAG, "____NOT CHANGED___");
                                    return -1;
                                }
                                else{
                                    //Log.d(TAG, "____CHANGED___");
                                    return 1;
                                }
                            }
                        };
                        Collections.sort(books, comparator);
                        Collections.sort(readingBooks, comparator);
                        Collections.sort(wannaReadBooks, comparator);
                        Collections.sort(completedBooks, comparator);
                        break;

                    case 1: //progress
                        Comparator<Book> comparator2 = new Comparator<Book>() {
                            @Override
                            public int compare(Book book, Book t1) {
                                if (t1.getTotalPages() != 0 && book.getTotalPages() != 0){
                                    //Log.d(TAG, "__NORMAL___("+book.getTitle()+")_"+book.getCurrentPage() / book.getTotalPages()+"&("+t1.getTitle()+")_"+book.getCurrentPage() / book.getTotalPages()+"___");
                                    return Float.compare(t1.getCurrentPage()*1.0f / t1.getTotalPages(), book.getCurrentPage()*1.0f / book.getTotalPages());}
                                else if (t1.getTotalPages() != 0){
                                    //Log.d(TAG, "__1 NOT ZERO___("+book.getTitle()+")&("+t1.getTitle()+")___");
                                    return 1;}
                                else if (book.getTotalPages() != 0){
                                    //Log.d(TAG, "__2 NOT ZERO___("+book.getTitle()+")&("+t1.getTitle()+")___");
                                    return -1;}
                                else{
                                    //Log.d(TAG, "__BOTH ZERO!___("+book.getTitle()+")&("+t1.getTitle()+")___");
                                    return 0;
                                }
                            }
                        };
                        Collections.sort(books, comparator2);
                        Collections.sort(readingBooks, comparator2);
                        Collections.sort(wannaReadBooks, comparator2);
                        Collections.sort(completedBooks, comparator2);
                        break;

                    case 2: //total pages
                        Comparator<Book> comparator3 = new Comparator<Book>() {
                            @Override
                            public int compare(Book book, Book t1) {
                                if (t1.getTotalPages() != 0 && book.getTotalPages() != 0){
                                    //Log.d(TAG, "__NORMAL___("+book.getTitle()+")_"+book.getCurrentPage() / book.getTotalPages()+"&("+t1.getTitle()+")_"+book.getCurrentPage() / book.getTotalPages()+"___");
                                    return Integer.compare(t1.getTotalPages(), book.getTotalPages());}
                                else if (t1.getTotalPages() != 0){
                                    //Log.d(TAG, "__1 NOT ZERO___("+book.getTitle()+")&("+t1.getTitle()+")___");
                                    return 1;}
                                else if (book.getTotalPages() != 0){
                                    //Log.d(TAG, "__2 NOT ZERO___("+book.getTitle()+")&("+t1.getTitle()+")___");
                                    return -1;}
                                else{
                                    //Log.d(TAG, "__BOTH ZERO!___("+book.getTitle()+")&("+t1.getTitle()+")___");
                                    return 0;
                                }
                            }
                        };
                        Collections.sort(books, comparator3);
                        Collections.sort(readingBooks, comparator3);
                        Collections.sort(wannaReadBooks, comparator3);
                        Collections.sort(completedBooks, comparator3);
                        break;

                    case 3: //current page
                        Comparator<Book> comparator4 = new Comparator<Book>() {
                            @Override
                            public int compare(Book book, Book t1) {
                                    //Log.d(TAG, "__NORMAL___("+book.getTitle()+")_"+book.getCurrentPage() / book.getTotalPages()+"&("+t1.getTitle()+")_"+book.getCurrentPage() / book.getTotalPages()+"___");
                                    return Integer.compare(t1.getCurrentPage(), book.getCurrentPage());
                            }
                        };
                        Collections.sort(books, comparator4);
                        Collections.sort(readingBooks, comparator4);
                        Collections.sort(wannaReadBooks, comparator4);
                        Collections.sort(completedBooks, comparator4);
                        break;

                    default:
                        break;
                }
                if (spinnerType.getSelectedItemPosition() == 1){
                    Collections.reverse(books);
                    Collections.reverse(readingBooks);
                    Collections.reverse(wannaReadBooks);
                    Collections.reverse(completedBooks);
                }
                switch (bottomNav.getSelectedItemId()){//determine current category
                    case R.id.readingMenu:
                        sortBooks = readingBooks;
                        break;
                    case R.id.wannaReadMenu:
                        sortBooks = wannaReadBooks;
                        break;
                    case R.id.completedMenu:
                        sortBooks = completedBooks;
                        break;
                    default:
                        sortBooks = books;
                        break;
                }
                Log.d(TAG, "____SPINNER SET BOOKS CALLED___");
                setBooks(sortBooks);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        if (isSpinnerTypeFirstTime){
                            isSpinnerTypeFirstTime = false;
                            break;
                        }
                    case 1:
                        Collections.reverse(books);
                        Collections.reverse(readingBooks);
                        Collections.reverse(wannaReadBooks);
                        Collections.reverse(completedBooks);
                        break;
                    default:
                        break;
                }
                switch (bottomNav.getSelectedItemId()){//determine current category
                    case R.id.readingMenu:
                        sortBooks = readingBooks;
                        break;
                    case R.id.wannaReadMenu:
                        sortBooks = wannaReadBooks;
                        break;
                    case R.id.completedMenu:
                        sortBooks = completedBooks;
                        break;
                    default:
                        sortBooks = books;
                        break;
                }

                setBooks(sortBooks);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sharedP = getSharedPreferences("dataFile", MODE_PRIVATE);
        editor = sharedP.edit();
        spIndex = sharedP.getInt("index",0);


        if (getIntent().getExtras() != null){
            bookNameFromIntent = getIntent().getExtras().getString("Name");
            currentPageFromIntent = getIntent().getExtras().getString("Page");
            haveReadFromIntent = getIntent().getExtras().getBoolean("HaveRead");
            sourceFromIntent = getIntent().getExtras().getString("Source");
            indexFromIntent = getIntent().getExtras().getInt("Index");
        }

        //books.add(new Book("Nicolas and Alexandra", "Robert K. Massei", "A Historical Book", "Finished", 445, 445));
        loadData();

        initRecView();

    }

    public void loadData() {
        String name, page, author, desc, tPage, coverUri, stateStr;
        int index, state;
        for (int i=0; i<spIndex; i++){
            name = sharedP.getString("Name"+i, "Unnamed book");
            if (name.equals("Unnamed book"))
                continue;
            else if (name.equals(""))
                name = "Unnamed";
            index = sharedP.getInt("SelfIndex"+i, -1);
            if (index == -1){ //  EXP:___ For handling the situation in which the user updates the program and its data has not the prametre "SelfIndex"
                editor.putInt("SelfIndex"+i, i);
                editor.commit();
            }
            coverUri = sharedP.getString("CoverUri"+i, "");
            if (coverUri.equals("")){
                editor.putString("CoverUri"+i, "");
                editor.commit();
            }
            page = sharedP.getString("Page"+i, "0");
            author = sharedP.getString("Author"+i, "Unknown artist");
            desc = sharedP.getString("Desc"+i, "No description...");
            tPage = sharedP.getString("Tpage"+i, "");
            stateStr = sharedP.getString("State"+i, "0");
            state = Integer.parseInt(sharedP.getString("State"+i, "0"));
            if (page.equals(""))
                page = "0";
            if (tPage.equals(""))
                tPage = "0";
            books.add(new Book(name, author, desc, state, Integer.parseInt(page), Integer.parseInt(tPage), index, coverUri));
            if (state == 0)
                wannaReadBooks.add(new Book(name, author, desc, state, Integer.parseInt(page), Integer.parseInt(tPage), index, coverUri));
            else if (state == 1)
                readingBooks.add(new Book(name, author, desc, state, Integer.parseInt(page), Integer.parseInt(tPage), index, coverUri));
            else if (state == 2)
                completedBooks.add(new Book(name, author, desc, state, Integer.parseInt(page), Integer.parseInt(tPage), index, coverUri));
        }
    }

    public void initBottomNav() {
        bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.allBooksMenu:
                        setBooks(books);
                        return true;
                    case R.id.readingMenu:
                        setBooks(readingBooks);
                        return true;
                    case R.id.wannaReadMenu:
                        setBooks(wannaReadBooks);
                        return true;
                    case R.id.completedMenu:
                        setBooks(completedBooks);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    public void initRecView() {
        recView = findViewById(R.id.recyclerView);
        //recViewAdapter.setBooks(books);
        recView.setAdapter(recViewAdapter);
        recView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void initFab() {
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, NewBookActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflator = getMenuInflater();
        inflator.inflate(R.menu.menu_main, menu);
        final MenuItem searchMenuItem = menu.findItem(R.id.searchMenu);
        searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setQueryHint(getString(R.string.search));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //recViewAdapter.filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                recViewAdapter.filter(s);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.aboutMenu:
                toaster2(getString(R.string.about_dialogue));
                return true;

            default:
                toaster1("Not Defiened");
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()){
            searchView.setIconified(true);
            searchView.onActionViewCollapsed();
        }
        else
            super.onBackPressed();
    }

    public void setBooks(ArrayList<Book> books){
        if (books.isEmpty())
            recViewPlaceholder.setVisibility(View.VISIBLE);
        else
            recViewPlaceholder.setVisibility(View.GONE);
        recViewAdapter.setBooks(books);
    }

    public void toaster1(String txt){ Toast.makeText(this, txt, Toast.LENGTH_SHORT).show(); }
    public void toaster2(String txt){ Toast.makeText(this, txt, Toast.LENGTH_LONG).show(); }



    //--TODO (IMPORTANT) : delete "handling old version" expressions in "loaddata()" function

    //FATURES
    //TODO: reading reminder
    //TODO: Sharing a book
    //TODO: backup and restore
    //TODO: Saving the date a book gets submited and completed (and edited maybe)

    //--TODO: list sorting
    //--TODO: list searching

    //?todo: get book information by an api from a service like goodreads or amazon
    //https://openlibrary.org/api/books?bibkeys=ISBN:0201558025,LCCN:93005405&format=json
    //?todo: getting the books cover images :: open library search api -> covers api
    //?todo check material.io

    //--todo: update NewBook and EditBook Activities
    //--todo use a Database
    //--TODO make a edit book activity
    //--todo: books catorization
    //--todo: Fix deleting problems
    //--TODO: image compression in 'editbookActivity' and 'newbookActivity' image picker
    //--TODO: beautify editbook and newbook layouts
    //--TODO: tidy ur code up! (delete obsolote comments)
}
