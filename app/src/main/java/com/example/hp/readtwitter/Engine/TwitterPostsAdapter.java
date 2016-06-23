package com.example.hp.readtwitter.Engine;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hp.readtwitter.R;
import com.example.hp.readtwitter.TwitterPost;

import java.util.List;

import static com.example.hp.readtwitter.Engine.Service.getTimeAgo;


public class TwitterPostsAdapter extends RecyclerView.Adapter<TwitterPostsAdapter.ViewHolder> {
    private List<TwitterPost> twitterPostsList;

    public TwitterPostsAdapter(List<TwitterPost> twitterPostsList) {
        this.twitterPostsList = twitterPostsList;
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, desc, date;
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            desc = (TextView) itemView.findViewById(R.id.desc);
            title = (TextView) itemView.findViewById(R.id.title);
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
        holder.title.setText(twitterPost.getTitle());
        holder.date.setText(getTimeAgo(twitterPost.getDate()));
        holder.desc.setText(twitterPost.getDescription());
    }

    @Override
    public int getItemCount() {
        return twitterPostsList.size();
    }


}
