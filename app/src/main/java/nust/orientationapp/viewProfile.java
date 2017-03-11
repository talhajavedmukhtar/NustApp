package nust.orientationapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class viewProfile extends Activity {
    private StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://orientationapp-328eb.appspot.com");

    private ImageView profilePicture;
    private TextView name;
    private TextView homeTown;
    private TextView profileType;
    private TextView major;
    private TextView school;
    private TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        Intent intent = getIntent();
        Person person = intent.getParcelableExtra("person");

        profilePicture = (ImageView) findViewById(R.id.profilePicture);
        name = (TextView) findViewById(R.id.name);
        homeTown = (TextView) findViewById(R.id.homeTown);
        profileType = (TextView) findViewById(R.id.profileType);
        major = (TextView) findViewById(R.id.major);
        school = (TextView) findViewById(R.id.school);
        description = (TextView) findViewById(R.id.description);


        storageRef.child("ProfilePics").child(person.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(viewProfile.this).load(uri).fit().centerInside().error(R.drawable.placeholder).placeholder(R.drawable.loading).into(profilePicture);
            }
        });

        name.setText(person.getFirstName() + " " + person.getLastName());
        homeTown.setText("from " + person.getHomeTown());
        profileType.setText(person.getProfileType());
        major.setText("enrolled in " + person.getMajor());
        school.setText("at " + person.getSchool());

        description.setText("Description:  " + person.getDescription());
    }

    public void closeProfile(View view){
        finish();
    }
}
