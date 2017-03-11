package nust.orientationapp;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class addInterestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_interest);
    }

    public void saveInterestBeacon(View view){
        MyApplication mApp = (MyApplication) getApplicationContext();
        String creatorId = mApp.getLoggedInId();

        String name = ((EditText)findViewById(R.id.interestName)).getText().toString();
        String type = ((EditText)findViewById(R.id.interestType)).getText().toString();
        String description = ((EditText)findViewById(R.id.interestBackground)).getText().toString();

        InterestBeacon interest = new InterestBeacon(creatorId,name,type,description);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("interestBeacons").push();
        String interestId = ref.getKey();
        interest.setInterestId(interestId);
        ref.setValue(interest);

        ref = FirebaseDatabase.getInstance().getReference().child("users").child("data").child(mApp.getLoggedInId());
        FirebaseDatabase.getInstance().getReference().child("users").child("interests").child(interestId).push().setValue(mApp.getLoggedInId());

        finish();
    }
}
