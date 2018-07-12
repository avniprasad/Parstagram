package codepathavniprasad.com.parstagram;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import codepathavniprasad.com.parstagram.model.Post;


public class PostDetailsFragment extends Fragment {

    public ImageView ivPostImage;
    public TextView tvUsername;
    public TextView tvTopUsername;
    public TextView tvCaption;
    public TextView tvTimestamp;
    public ImageView ibHeart;
    public ImageView ibComment;
    public ImageView ibShare;
    public ImageView ivUserPic;

    public RecyclerView rvComments;
    public CommentAdapter commentAdapter;
    public List<Object> comments;

    private static final String KEY_USER_PICTURE = "profileImage";

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
        tvTopUsername = (TextView) itemView.findViewById(R.id.tvTopUsername);
        tvCaption = (TextView) itemView.findViewById(R.id.tvCaption);
        tvTimestamp = (TextView) itemView.findViewById(R.id.tvTimestamp);
        ibHeart = (ImageView) itemView.findViewById(R.id.heart_btn);
        ibComment = (ImageView) itemView.findViewById(R.id.comment_btn);
        ibShare = (ImageView) itemView.findViewById(R.id.share_btn);
        ivUserPic = (ImageView) itemView.findViewById(R.id.profile_iv);
        rvComments = (RecyclerView) itemView.findViewById(R.id.rv_comments);

        comments = new ArrayList<>();
        commentAdapter = new CommentAdapter(comments);
        rvComments.setLayoutManager(new LinearLayoutManager(getContext()));
        rvComments.setAdapter(commentAdapter);

        Bundle args = getArguments();
        String id = args.getString("PostId");

        Post.Query query = new Post.Query().withUser();
        query.getQuery(Post.class).getInBackground(id, new GetCallback<Post>() {
            @Override
            public void done(Post post, ParseException e) {
                // assign Views values in PostDetailsFragment
                try {
                    tvUsername.setText(post.getUser().fetchIfNeeded().getUsername());
                    tvTopUsername.setText(post.getUser().fetchIfNeeded().getUsername());
                    tvCaption.setText(post.getDescription());
                    // String date = getRelativeTimeAgo(tweet.createAt);
                    tvTimestamp.setText(post.getRelativeTimeAgo().toUpperCase());

                    GlideApp.with(getContext())
                            .load(post.getImage().getUrl())
                            .into(ivPostImage);

                    ParseUser postUser = post.getUser();
                    if(postUser.get(KEY_USER_PICTURE) != null) {
                        ParseFile photoFile = postUser.getParseFile(KEY_USER_PICTURE);
                        GlideApp.with(getContext()).load(photoFile.getUrl()).circleCrop()
                                .into(ivUserPic);
                    }

                    comments.clear();
                    comments.addAll(post.getList("comments"));
                    commentAdapter.notifyDataSetChanged();

                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

}
