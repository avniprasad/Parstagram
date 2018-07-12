package codepathavniprasad.com.parstagram;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;

import codepathavniprasad.com.parstagram.model.Post;


public class PostDetailsFragment extends Fragment {

    public ImageView ivPostImage;
    public TextView tvUsername;
    public TextView tvCaption;
    public TextView tvTimestamp;
    public ImageView ibHeart;
    public ImageView ibComment;
    public ImageView ibShare;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_details, container, false);
    }

    @Override
    public void onViewCreated(View itemView, Bundle savedInstanceState) {
        super.onViewCreated(itemView, savedInstanceState);

        // initiate all the Views in PostDetailsFragment
        // perform findViewById lookups
        ivPostImage = (ImageView) itemView.findViewById(R.id.ivPostImage);
        tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
        tvCaption = (TextView) itemView.findViewById(R.id.tvCaption);
        tvTimestamp = (TextView) itemView.findViewById(R.id.tvTimestamp);
        ibHeart = (ImageView) itemView.findViewById(R.id.heart_btn);
        ibComment = (ImageView) itemView.findViewById(R.id.comment_btn);
        ibShare = (ImageView) itemView.findViewById(R.id.share_btn);


        Bundle args = getArguments();
        String id = args.getString("PostId");

        Post.Query query = new Post.Query().withUser();
        query.getQuery(Post.class).getInBackground(id, new GetCallback<Post>() {
            @Override
            public void done(Post post, ParseException e) {
                // assign Views values in PostDetailsFragment
                try {
                    tvUsername.setText(post.getUser().fetchIfNeeded().getUsername());
                    tvCaption.setText(post.getDescription());
                    // String date = getRelativeTimeAgo(tweet.createAt);
                    tvTimestamp.setText(post.getRelativeTimeAgo());

                    GlideApp.with(getContext())
                            .load(post.getImage().getUrl())
                            .into(ivPostImage);
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

}
