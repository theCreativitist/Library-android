package com.fmgames.library;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import java.util.ArrayList;
import java.util.Random;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    Context context;
    ArrayList<String> colors = new ArrayList<>();
    Random random = new Random();
    int lastRand = 0;


    public RecyclerViewAdapter(Context context) {
        this.context = context;

        /*colors.add("#0074D9");
        colors.add("#7FDBFF");
        colors.add("#39CCCC");
        colors.add("#3D9970");
        colors.add("#2ECC40");
        colors.add("#01FF70");
        colors.add("#FFDC00");
        colors.add("#FF851B");
        colors.add("#FF4136");
        colors.add("#85144b");
        colors.add("#F012BE");
        colors.add("#B10DC9");*/
    }

    private ArrayList<Book> books = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Fresco.initialize(context);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_recyclerview, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: started");

        holder.bookName.setText(books.get(position).getTitle());
        holder.bookAuthor.setText(books.get(position).getAuthor());
        holder.currentPage.setText(books.get(position).getCurrentPageString());
        holder.desc.setText(books.get(position).getDescription());
        if (books.get(position).getTotalPagesString().equals("0")) {
            holder.pagesString.setText("");
            holder.totalPages.setText("");
            holder.progressBar.setVisibility(View.INVISIBLE);
            holder.percent.setVisibility(View.INVISIBLE);
        } else {
            holder.progressBar.setVisibility(View.VISIBLE);
            holder.percent.setVisibility(View.VISIBLE);
            holder.pagesString.setText(context.getResources().getString(R.string.pages));
            holder.totalPages.setText(books.get(position).getTotalPagesString());
            float percentage = (books.get(position).getCurrentPage() * 1.0f / books.get(position).getTotalPages()) * 100;
            String percentStr = String.format("%.0f", percentage) + "%";
            holder.percent.setText(percentStr);
        }

        if (books.get(position).getCurrentPageString().equals("0"))
            holder.currentPage.setVisibility(View.INVISIBLE);
        else
            holder.currentPage.setVisibility(View.VISIBLE);

        int stateInt = books.get(position).getState();
        if (stateInt == 0){
            holder.HaveRead.setTextColor(Color.RED);
            holder.HaveRead.setText(context.getString(R.string.wanna_read));
        }
        else if (stateInt == 1){
            holder.HaveRead.setTextColor(Color.BLUE);
            holder.HaveRead.setText(context.getString(R.string.reading));
        }
        else if (stateInt == 2){
            holder.HaveRead.setTextColor(context.getResources().getColor(R.color.green));
            holder.HaveRead.setText(context.getString(R.string.completed));
        }


        //holder.draweeView.setImageURI(Uri.parse(books.get(position).getCoverUri()));
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(books.get(position).getCoverUri()))
                    .setResizeOptions(new ResizeOptions(250, 375))
                    .build();
        holder.draweeView.setController(
                Fresco.newDraweeControllerBuilder()
                        .setOldController(holder.draweeView.getController())
                        .setImageRequest(request)
                        .build());
        /*else {
            int randColor;
            do {
                randColor = Color.parseColor(colors.get(random.nextInt(colors.size())));
            } while (randColor == lastRand);
            lastRand = randColor;
            holder.draweeView.setColorFilter(randColor, android.graphics.PorterDuff.Mode.MULTIPLY);
        }*/


        /*holder.relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent ebIntent = new Intent(context, EditBookActivity.class);
                ebIntent.putExtra("Index", books.get(position).getIndex());
                context.startActivity(ebIntent);
                return true;
            }
        });*/

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ebIntent = new Intent(context, EditBookActivity.class);
                ebIntent.putExtra("Index", books.get(position).getIndex());
                context.startActivity(ebIntent);
            }
        });

        holder.progressBar.setMax(books.get(position).getTotalPages());
        holder.progressBar.setProgress(books.get(position).getCurrentPage());
        //Glide.with(context).asBitmap().load("http://covers.openlibrary.org/b/ID/9371511-L.jpg").into(holder.image);

    }

    @Override
    public int getItemCount() {
        return books.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView bookName, bookAuthor, HaveRead, currentPage, totalPages, pagesString, percent, desc;
        RelativeLayout relativeLayout;
        ProgressBar progressBar;
        SimpleDraweeView draweeView;

        public ViewHolder(View itemView){
            super(itemView);

            relativeLayout = itemView.findViewById(R.id.relativeLayout);
            bookName = itemView.findViewById(R.id.listItemBookName);
            bookAuthor = itemView.findViewById(R.id.listItemBookAuthor);
            HaveRead = itemView.findViewById(R.id.listItemBookHaveRead);
            currentPage = itemView.findViewById(R.id.listItemBookCurrentPage);
            totalPages = itemView.findViewById(R.id.listItemBookTotalPages);
            pagesString = itemView.findViewById(R.id.listItemBookTotalPagesString);
            progressBar = itemView.findViewById(R.id.progressBar);
            percent = itemView.findViewById(R.id.percent);
            desc = itemView.findViewById(R.id.listItemDesc);
            draweeView = itemView.findViewById(R.id.listItemImageView);
        }
    }

    public void setBooks(ArrayList<Book> books) {
        this.books = books;
        notifyDataSetChanged();
    }
}
