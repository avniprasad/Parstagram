package codepathavniprasad.com.parstagram;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import codepathavniprasad.com.parstagram.model.Post;

public class NavigationActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private FragmentTransaction fragmentTransaction;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    Fragment fragment1 = new HomeFragment();
                    fragmentTransaction.replace(R.id.my_fragment, fragment1).commit();
                    return true;
                case R.id.navigation_dashboard:
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    Fragment fragment2 = new CameraFragment();
                    fragmentTransaction.replace(R.id.my_fragment, fragment2).commit();
                    return true;
                case R.id.navigation_notifications:
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    Fragment fragment3 = new ProfileFragment();
                    fragmentTransaction.replace(R.id.my_fragment, fragment3).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    protected void openDetails(Post post) {
        Fragment details_fragment = new PostDetailsFragment();
        // communicating from fragment to activity
        Bundle args = new Bundle();
        args.putString("PostId", post.getObjectId());
        details_fragment.setArguments(args);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.my_fragment, details_fragment).commit();
    }

}
