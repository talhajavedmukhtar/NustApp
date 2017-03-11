package nust.orientationapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ImageView;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Array;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Talha on 8/19/16.
 */
public class Person implements Parcelable{
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String school;
    private String homeTown;
    private String profileType;
    private String major;
    private String description;
    private boolean hostel;
    private String uid;
    //private String[] interestIds;
    private int unread;

    Person(){}

    Person(String fn, String ln, String email, String password, String school, String homeTown, String profileType, String uid){
        this.firstName = fn;
        this.lastName = ln;
        this.email = email;
        this.password = password;
        this.school = school;
        this.homeTown = homeTown;
        this.profileType = profileType;
        this.major = "";
        this.description = "";
        this.hostel = false;
        this.uid = uid;
        //this.interestIds = new String[]{"0","0","0","0","0"};
    }

    Person(String fn, String ln, String email, String password, String school, String homeTown, String profileType, String uid, int unread){
        this.firstName = fn;
        this.lastName = ln;
        this.email = email;
        this.password = password;
        this.school = school;
        this.homeTown = homeTown;
        this.profileType = profileType;
        this.major = "";
        this.description = "";
        this.hostel = false;
        this.uid = uid;
        this.unread = unread;
    }

    protected Person(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        email = in.readString();
        password = in.readString();
        school = in.readString();
        homeTown = in.readString();
        profileType = in.readString();
        major = in.readString();
        description = in.readString();
        hostel = in.readByte() != 0;
        uid = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(school);
        dest.writeString(homeTown);
        dest.writeString(profileType);
        dest.writeString(major);
        dest.writeString(description);
        dest.writeByte((byte) (hostel ? 1 : 0));
        dest.writeString(uid);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getHomeTown() {
        return homeTown;
    }

    public void setHomeTown(String homeTown) {
        this.homeTown = homeTown;
    }

    public String getProfileType() {
        return profileType;
    }

    public void setProfileType(String profileType) {
        this.profileType = profileType;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isHostel() {
        return hostel;
    }

    public void setHostel(boolean hostel) {
        this.hostel = hostel;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }
}
