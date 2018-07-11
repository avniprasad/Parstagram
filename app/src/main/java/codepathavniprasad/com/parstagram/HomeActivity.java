package codepathavniprasad.com.parstagram;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.File;
import java.util.List;

import codepathavniprasad.com.parstagram.model.Post;

public class HomeActivity extends AppCompatActivity {

    public final static int PICK_PHOTO_CODE = 1046;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;

    private static final String imagePath = "/storage/emulated/0/DCIM/Camera/IMG_20180709_175322.jpg";
    // TODO -- get image from user
    private EditText descriptionInput;
    private Button createButton;
    private Button refreshButton;

    /**

    ParseFile parseFile;
    public final String APP_TAG = "MyCustomApp";
    public String photoFileName = "photo.jpg";
    File photoFile;
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        descriptionInput = findViewById(R.id.description_et);
        createButton = findViewById(R.id.create_btn);
        refreshButton = findViewById(R.id.refresh_btn);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String description = descriptionInput.getText().toString();
                final ParseUser user = ParseUser.getCurrentUser();

                final File file = new File(imagePath);
                Log.d("HomeActivity","File successfully created");
                final ParseFile parseFile = new ParseFile(file);

                createPost(description, parseFile, user);
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadTopPosts();
            }
        });
    }

    private void createPost(final String description, final ParseFile imageFile, final ParseUser user) {
        Log.d("HomeActivity","New Post is saved");
        Post newPost = Post.newInstance(user, imageFile, description);
    }

    private void loadTopPosts() {
        final Post.Query postQuery = new Post.Query();
        postQuery.getTop().withUser();

        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i += 1) {
                        Log.d("HomeActivity", "Post[" + i + "] = "
                                + objects.get(i).getDescription()
                                + "\nusername = " + objects.get(i).getUser().getUsername());
                        // checking if grabbing right info and post object unwraps the user
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}
