package com.fmgames.library;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    Context context;
    int pos;

    public RecyclerViewAdapter(Context context) {
        this.context = context;
    }

    private ArrayList<Book> books = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_recyclerview, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: started");

        holder.bookName.setText(books.get(position).getTitle());
        holder.bookAuthor.setText(books.get(position).getAuthor());
        holder.HaveRead.setText(books.get(position).getState());
        holder.currentPage.setText(books.get(position).getCurrentPageString());
        holder.totalPages.setText(books.get(position).getTotalPagesString());

        holder.relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent ebIntent = new Intent(context, EditBookActivity.class);
                ebIntent.putExtra("Name", books.get(position).getTitle());
                ebIntent.putExtra("Page", books.get(position).getCurrentPageString());
                ebIntent.putExtra("Index", position);
                //ebIntent.putExtra("HaveRead", haveReadBool);
                context.startActivity(ebIntent);
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return books.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        //ImageView image;
        TextView bookName, bookAuthor, HaveRead, currentPage, totalPages;
        RelativeLayout relativeLayout;

        public ViewHolder(View itemView){
            super(itemView);

            //image = itemView.findViewById(R.id.listItemImageView);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
            bookName = itemView.findViewById(R.id.listItemBookName);
            bookAuthor = itemView.findViewById(R.id.listItemBookAuthor);
            HaveRead = itemView.findViewById(R.id.listItemBookHaveRead);
            currentPage = itemView.findViewById(R.id.listItemBookCurrentPage);
            totalPages = itemView.findViewById(R.id.listItemBookTotalPages);
        }
    }

    public void setBooks(ArrayList<Book> books) {
        this.books = books;
        notifyDataSetChanged();
    }
}
