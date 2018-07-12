package codepathavniprasad.com.parstagram;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import codepathavniprasad.com.parstagram.model.Post;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    private List<Post> mPosts;
    private Context context;

    // pass in the Post array in the constructor
    public PostAdapter(List<Post> posts) {
        mPosts = posts;
    }

    // for each row, inflate the layout and cache reference into ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View postView = inflater.inflate(R.layout.item_post, parent, false);
        ViewHolder viewHolder = new ViewHolder(postView);
        return viewHolder;
    }

    // bind the values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        // get the data according to position
        Post post = mPosts.get(position);

        // populate the views according to this data
        holder.tvUsername.setText(post.getUser().getUsername());
        holder.tvCaption.setText(post.getDescription());
        // String date = getRelativeTimeAgo(tweet.createAt);
        holder.tvTimestamp.setText(post.getRelativeTimeAgo().toUpperCase());

        /**
        holder.ibRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.retweetTweet(mTweets.get(position).getUid(), handler);
                Toast.makeText(context, "Retweeted", Toast.LENGTH_LONG).show();
            }
        });

        holder.ibLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.likeTweet(mTweets.get(position).getUid(), handler);
                Toast.makeText(context, "Liked Tweet", Toast.LENGTH_LONG).show();
            }
        });
         */


        GlideApp.with(context)
                .load(post.getImage().getUrl())
                .into(holder.ivPostImage);

        /**
        holder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, UserInfoActivity.class);
                i.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(mTweets.get(position)));
                context.startActivity(i);
            }
        });
         */
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    /* Within the RecyclerView.Adapter class */

    // Clean all elements of the recycler
    public void clear() {
        mPosts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        mPosts.addAll(list);
        notifyDataSetChanged();
    }

    // create ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView ivPostImage;
        public TextView tvUsername;
        public TextView tvTopUsername;
        public TextView tvCaption;
        public TextView tvTimestamp;
        public ImageView ibHeart;
        public ImageView ibComment;
        public ImageView ibShare;


        public ViewHolder(View itemView) {
            super(itemView);

            // perform findViewById lookups
            ivPostImage = (ImageView) itemView.findViewById(R.id.ivPostImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            tvTopUsername = (TextView) itemView.findViewById(R.id.tvTopUsername);
            tvCaption = (TextView) itemView.findViewById(R.id.tvCaption);
            tvTimestamp = (TextView) itemView.findViewById(R.id.tvTimestamp);
            ibHeart = (ImageView) itemView.findViewById(R.id.heart_btn);
            ibComment = (ImageView) itemView.findViewById(R.id.comment_btn);
            ibShare = (ImageView) itemView.findViewById(R.id.share_btn);

            itemView.setOnClickListener(this);
        }


        // when the user clicks on a row, show PostDetailsFragments the selected tweet
        @Override
        public void onClick(View view) {
            // gets item position
            Log.d("PostAdapter", String.format("A post was clicked"));
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the post at the position, this won't work if the class is static
                Post post = mPosts.get(position);
                Log.d("PostAdapter", String.format("Got the tweet: " + post.getDescription()));
                ((NavigationActivity) context).openDetails(post);
            }
        }

    }

    /**
    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
     */
}
