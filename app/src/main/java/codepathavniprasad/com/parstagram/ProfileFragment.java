package codepathavniprasad.com.parstagram;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import codepathavniprasad.com.parstagram.model.Post;


public class ProfileFragment extends Fragment {

    private static final String KEY_USER_BIO = "bio";
    private static final String KEY_USER_PICTURE = "profileImage";

    Button logoutButton;
    Button addBioBtn;
    Button editBioBtn;
    EditText addBioText;
    TextView currentText;
    TextView usernameText;
    ImageView camBut;
    ImageView profPic;
    TextView numPostText;

    RecyclerView rvPosts;
    PostAdapter postAdapter;
    List<Post> posts;
    int numPosts = 0;

    ParseUser user;

    public static final int REQUEST_IMAGE_CAPTURE = 1;

    public final String APP_TAG = "MyCustomApp";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "photo.jpg";
    File photoFile;

    public Bitmap imageBitmap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        logoutButton = view.findViewById(R.id.logout_btn);

        addBioBtn = view.findViewById(R.id.addBio_btn);
        addBioText = view.findViewById(R.id.addBio_et);

        editBioBtn = view.findViewById(R.id.editBio_btn);
        currentText = view.findViewById(R.id.bio_tv);

        usernameText = view.findViewById(R.id.username_tv);
        camBut = view.findViewById(R.id.camera_iv);
        profPic = view.findViewById(R.id.profile_iv);
        rvPosts = (RecyclerView) view.findViewById(R.id.rvPosts);
        numPostText = view.findViewById(R.id.posts_tv);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                Intent i = new Intent(getActivity(), LoginActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });

        user = ParseUser.getCurrentUser();

        usernameText.setText(user.getUsername());

        if(user.get(KEY_USER_PICTURE) != null) {
            ParseFile photoFile = user.getParseFile(KEY_USER_PICTURE);
            GlideApp.with(getContext()).load(photoFile.getUrl()).circleCrop()
                    .into(profPic);
        }

        if (user.get(KEY_USER_BIO) == null) {
            editBioBtn.setVisibility(View.INVISIBLE);
            currentText.setVisibility(View.INVISIBLE);
            Log.d("ProfileFragment","There is no bio we have saved");
        } else {
            Log.d("ProfileFragment","We have saved a bio from your profile");
            String bio = user.get(KEY_USER_BIO).toString();
            addBioBtn.setVisibility(View.INVISIBLE);
            addBioText.setVisibility(View.INVISIBLE);
            currentText.setText(bio);
        }

        addBioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bio = addBioText.getText().toString();
                addBioBtn.setVisibility(View.INVISIBLE);
                addBioText.setVisibility(View.INVISIBLE);
                editBioBtn.setVisibility(View.VISIBLE);
                currentText.setVisibility(View.VISIBLE);
                currentText.setText(bio);
                user.put(KEY_USER_BIO, bio);
                user.saveInBackground();
            }
        });

        editBioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBioBtn.setVisibility(View.VISIBLE);
                addBioText.setVisibility(View.VISIBLE);
                editBioBtn.setVisibility(View.INVISIBLE);
                currentText.setVisibility(View.INVISIBLE);
            }
        });

        camBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Create a File reference to access to future access

                photoFile = getPhotoFileUri(photoFileName);

                // wrap File object into a content provider
                // required for API >= 24
                // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
                Uri fileProvider = FileProvider.getUriForFile(getActivity(), "codepathavniprasad.com.parstagram", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

                if (takePictureIntent.resolveActivity(ProfileFragment.this.getActivity().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        posts = new ArrayList<Post>();
        postAdapter = new PostAdapter(posts);
        // RecyclerView setup (layout manager, use adapter)
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        // set the adapter
        rvPosts.setAdapter(postAdapter);

        loadPosts();
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(APP_TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == -1) {
            Bundle extras = data.getExtras();
            imageBitmap = (BitmapFactory.decodeFile(photoFile.getAbsolutePath()));
            ParseFile parseFile = new ParseFile(photoFile);
            user.put(KEY_USER_PICTURE, parseFile);
            user.saveInBackground();
            profPic.setImageBitmap(imageBitmap);
        }
    }

    private void loadPosts() {
        final Post.Query postQuery = new Post.Query();
        postQuery.getTop().withUser();

        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i += 1) {
                        Log.d("ProfileFragment", "Post[" + i + "] = "
                                + objects.get(i).getDescription()
                                + "\nusername = " + objects.get(i).getUser().getUsername());
                        // checking if grabbing right info and post object unwraps the user
                        if (objects.get(i).getUser().getUsername().equals(user.getUsername())) {
                            posts.add(0, objects.get(i));
                            postAdapter.notifyItemInserted(posts.size() - 1);
                        }
                    }
                    numPostText.setText("" + posts.size());
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}
