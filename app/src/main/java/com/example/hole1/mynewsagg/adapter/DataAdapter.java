package com.example.hole1.mynewsagg.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hole1.mynewsagg.R;
import com.example.hole1.mynewsagg.model.ArticleStructure;
import com.example.hole1.mynewsagg.model.Constants;
import com.example.hole1.mynewsagg.view.WebViewActivity;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private ArrayList<ArticleStructure> articles;
    private Context mContext;
    private int lastPosition = -1;

    public DataAdapter(Context mContext, ArrayList<ArticleStructure> articles) {
        this.mContext = mContext;
        this.articles = articles;
    }

    /*
     ** inflating the cardView.
     **/
    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder holder, int position) {

        String title = articles.get(position).getTitle();
         if(title.endsWith(" - Firstpost"))
            title = title.replace(" - Firstpost", "");


        holder.tv_card_main_title.setText(title);

        Glide.with(mContext)
                .load(articles.get(position).getUrlToImage())
                .thumbnail(0.1f)
                .centerCrop()
                .error(R.drawable.ic_message_black_24dp)
                .into(holder.img_card_main);

        if (position > lastPosition) {
            lastPosition = position;
        }
    }

    /*
     ** Last parameter for binding the articles in OnBindViewHolder.
     **/
    @Override
    public int getItemCount() {
        return articles.size();
    }

    /*
     ** ViewHolder class which holds the different views in the recyclerView .
     **/
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv_card_main_title;
        private ImageView img_card_main;
        private CardView cardView;

        public ViewHolder(View view) {
            super(view);
            tv_card_main_title = view.findViewById(R.id.tv_card_main_title);
            img_card_main = view.findViewById(R.id.img_card_main);
            cardView = view.findViewById(R.id.card_row);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            String URL = articles.get(getAdapterPosition()).getUrl();
            openWebViewActivity(URL);
        }

        private void openWebViewActivity(String URL) {
            Intent browserIntent = new Intent(mContext, WebViewActivity.class);
            browserIntent.putExtra(Constants.INTENT_URL, URL);
            mContext.startActivity(browserIntent);
        }
    }
}
