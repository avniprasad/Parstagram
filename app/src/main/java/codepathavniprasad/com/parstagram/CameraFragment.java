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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

import codepathavniprasad.com.parstagram.model.Post;


public class CameraFragment extends Fragment {

    private static final String imagePath = "/storage/emulated/0/DCIM/Camera/IMG_20180709_175322.jpg";
    private EditText descriptionInput;
    // private Button createButton;
    // private Button refreshButton;
    private Button camButton;
    private Button postButton;
    private ImageView postPic;
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
        return inflater.inflate(R.layout.fragment_camera, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        descriptionInput = view.findViewById(R.id.description_et);
        // createButton = view.findViewById(R.id.create_btn);
        // refreshButton = view.findViewById(R.id.refresh_btn);
        camButton = view.findViewById(R.id.cam_btn);
        postButton = view.findViewById(R.id.post_btn);
        postPic = view.findViewById(R.id.ivPostPic);

        postButton.setVisibility(View.INVISIBLE);
        postPic.setVisibility(View.INVISIBLE);
        descriptionInput.setVisibility(View.INVISIBLE);

        camButton.setOnClickListener(new View.OnClickListener() {
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

                if (takePictureIntent.resolveActivity(CameraFragment.this.getActivity().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String message = descriptionInput.getText().toString();
                final ParseUser user = ParseUser.getCurrentUser();

                final File file = getPhotoFileUri(photoFileName);
                final ParseFile parseFile = new ParseFile(file);

                parseFile.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            createPost(message, parseFile, user);
                            Log.d("CameraFragment", "parse file successfully saved in background");
                        } else {
                            Log.d("CameraFragment", "parse file failed to saved in background");
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == -1) {
            Bundle extras = data.getExtras();
            imageBitmap = (BitmapFactory.decodeFile(photoFile.getAbsolutePath()));
            postPic.setImageBitmap(imageBitmap);

            // Uri orgUri = data.getData();
            // Log.d("CameraFragment", "URI: " + orgUri);

            postPic.setVisibility(View.VISIBLE);
            descriptionInput.setVisibility(View.VISIBLE);
            camButton.setVisibility(View.INVISIBLE);
            postButton.setVisibility(View.VISIBLE);
        }
    }

    private void createPost(final String description, final ParseFile imageFile, final ParseUser user) {
        Log.d("HomeActivity","New Post is saved");
        Post newPost = Post.newInstance(user, imageFile, description);

        postButton.setVisibility(View.INVISIBLE);
        postPic.setVisibility(View.INVISIBLE);
        descriptionInput.setVisibility(View.INVISIBLE);
        camButton.setVisibility(View.VISIBLE);
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
}
