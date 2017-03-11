package nust.orientationapp;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Talha on 8/19/16.
 */
public class MyApplication extends Application {
    private String loggedInId;
    private Person loggedInPerson;

    public Person getLoggedInPerson() {
        return loggedInPerson;
    }

    public void setLoggedInPerson(Person loggedInPerson) {
        this.loggedInPerson = loggedInPerson;
    }

    public String getLoggedInId() {
        return loggedInId;
    }

    public void setLoggedInId(String loggedInId) {
        this.loggedInId = loggedInId;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
