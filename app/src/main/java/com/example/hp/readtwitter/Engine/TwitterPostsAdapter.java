package com.example.hp.readtwitter.Engine;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hp.readtwitter.R;
import com.example.hp.readtwitter.TwitterClass.TwitterPost;

import java.util.List;

import static com.example.hp.readtwitter.Engine.Service.getTimeAgo;


public class TwitterPostsAdapter extends RecyclerView.Adapter<TwitterPostsAdapter.ViewHolder> {
    private List<TwitterPost> twitterPostsList;

    public TwitterPostsAdapter(List<TwitterPost> twitterPostsList) {
        this.twitterPostsList = twitterPostsList;
    }
    
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView text, desc, date, ur;
        ImageView image;
        Bitmap scr;

        public ViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text);
            date = (TextView) itemView.findViewById(R.id.date);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TwitterPost twitterPost = twitterPostsList.get(position);

        holder.date.setText(getTimeAgo(twitterPost.getDate()));
        holder.text.setText(twitterPost.getText(false));

    }

    @Override
    public int getItemCount() {
        return twitterPostsList.size();
    }


}
