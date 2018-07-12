package codepathavniprasad.com.parstagram;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import codepathavniprasad.com.parstagram.model.Post;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{

    private List<Object> mComments;
    private Context context;

    public ParseUser user;

    // pass in the Comments array in the constructor
    public CommentAdapter(List<Object> comments) {
        mComments = comments;
    }

    // for each row, inflate the layout and cache reference into ViewHolder
    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View commentView = inflater.inflate(R.layout.item_comment, parent, false);
        CommentAdapter.ViewHolder viewHolder = new CommentAdapter.ViewHolder(commentView);
        return viewHolder;
    }

    // bind the values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull final CommentAdapter.ViewHolder holder, final int position) {
        String comment = (String) mComments.get(position);

        final String[] commentSplit = comment.split(" ", 2);
        String userId = commentSplit[0];

        ParseQuery<ParseUser> commentUser = ParseUser.getQuery().whereEqualTo("objectId", userId);
        commentUser.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                for (int i = 0; i < objects.size(); i += 1) {
                    user = objects.get(i);

                    GlideApp.with(context).load(user.getParseFile("profileImage").getUrl()).circleCrop()
                            .into(holder.ivUserImage);

                    holder.tvComment.setText(Html.fromHtml("<b>" + user.getUsername() + "</b>  " + commentSplit[1]));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    /* Within the RecyclerView.Adapter class */

    // Clean all elements of the recycler
    public void clear() {
        mComments.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        mComments.addAll(list);
        notifyDataSetChanged();
    }

    // create ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivUserImage;
        public TextView tvComment;


        public ViewHolder(View itemView) {
            super(itemView);

            // perform findViewById lookups
            ivUserImage = (ImageView) itemView.findViewById(R.id.userPic);
            tvComment = (TextView) itemView.findViewById(R.id.userComment);
        }

    }

}
