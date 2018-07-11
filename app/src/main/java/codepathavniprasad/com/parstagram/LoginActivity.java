package codepathavniprasad.com.parstagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameInput;
    private EditText passwordInput;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            Intent i = new Intent(LoginActivity.this, NavigationActivity.class);
            startActivity(i);
            Toast.makeText(LoginActivity.this, "Automatically logged in!", Toast.LENGTH_LONG).show();
        }

        usernameInput = findViewById(R.id.tvUsername);
        passwordInput = findViewById(R.id.tvPassword);
        loginBtn = findViewById(R.id.bLogin);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = usernameInput.getText().toString();
                final String password = passwordInput.getText().toString();

                login(username, password);
            }
        });
    }

    private void login(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) { // if there is no error
                    Log.d("LoginActivity", "Login successful!");
                    final Intent intent = new Intent(LoginActivity.this, NavigationActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e("LoginActivity", "Login failure.");
                    e.printStackTrace();
                }
            }
        });
    }


    public void onLoginButton(View v) {
        /**
        Intent i = new Intent(this, EnteredActivity.class);
        startActivity(i);
         */
    }

}
