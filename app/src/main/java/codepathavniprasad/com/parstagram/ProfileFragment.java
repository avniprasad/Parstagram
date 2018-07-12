package codepathavniprasad.com.parstagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseUser;


public class ProfileFragment extends Fragment {

    private static final String KEY_USER_BIO = "bio";

    Button logoutButton;
    Button addBioBtn;
    Button editBioBtn;
    EditText addBioText;
    TextView currentText;
    TextView usernameText;

    ParseUser user;

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
    }
}
