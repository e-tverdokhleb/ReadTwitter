package com.example.hp.readtwitter.Engine;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hp.readtwitter.MainActivity;
import com.example.hp.readtwitter.R;
import com.example.hp.readtwitter.TwitterServiceClass.TwitterPost;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.hp.readtwitter.Engine.Service.getTimeAgo;


public class TwitterPostsAdapter extends RecyclerView.Adapter<TwitterPostsAdapter.ViewHolder> {
    private List<TwitterPost> twitterPostsList;




    public TwitterPostsAdapter(List<TwitterPost> twitterPostsList) {
        this.twitterPostsList = twitterPostsList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView text, desc, date, url;
        ImageView image;

     /*  @BindView(R.id.text)
        TextView text;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.url)
        TextView url;
        @BindView(R.id.imageView)
        ImageView image;
*/
        public ViewHolder(View itemView) {
            super(itemView);

          //  ButterKnife.bind(this, itemView);

            text = (TextView) itemView.findViewById(R.id.text);
            date = (TextView) itemView.findViewById(R.id.date);
            url = (TextView) itemView.findViewById(R.id.url);
            image = (ImageView) itemView.findViewById(R.id.imageView);
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

        if ((twitterPost.getMediaUrl() != "")) {
            Picasso.with(MainActivity.getContext())
                    .load(twitterPost.getMediaUrl())
                    .placeholder(R.mipmap.twitter_image_loading)
                    .error(R.mipmap.twitter_image_loading_error)
                    .into(holder.image);
        } else holder.image.setImageResource(R.mipmap.twitter_image_default);
    }

    @Override
    public int getItemCount() {
        return twitterPostsList.size();
    }


}
