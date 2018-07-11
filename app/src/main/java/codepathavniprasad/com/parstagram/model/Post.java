package codepathavniprasad.com.parstagram.model;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

@ParseClassName("Post")
public class Post extends ParseObject{
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_USER = "user";

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile image) {
        put(KEY_IMAGE, image);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public static class Query extends ParseQuery<Post>{
        public Query() {
            super(Post.class);
        }

        public Query getTop() {
            setLimit(20);
            return this; // builder pattern
        }

        public Query withUser() {
            include("user");
            return this;
        }
    }

    public static Post newInstance(ParseUser user, ParseFile file, String description) {
        final Post newPost = new Post();
        newPost.setDescription(description);
        newPost.setUser(user);
        newPost.setImage(file);

        newPost.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) { // if there is no error
                    Log.d("Post", "Post save successful!");
                } else {
                    Log.e("Post", "Post save failure.");
                    e.printStackTrace();
                }
            }
        });
        return newPost;
    }
}
