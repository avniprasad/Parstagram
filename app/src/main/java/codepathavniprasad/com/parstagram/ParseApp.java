package codepathavniprasad.com.parstagram;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

import codepathavniprasad.com.parstagram.model.Post;

public class ParseApp extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);
        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("pingpong")
                .clientKey("betty-bought-some-bitter-butter")
                .server("http://avniprasad-fbu-instagram.herokuapp.com/parse")
                .build();

        Parse.initialize(configuration);
    }
}
