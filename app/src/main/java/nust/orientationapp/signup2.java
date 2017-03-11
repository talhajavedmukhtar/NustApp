package nust.orientationapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;

public class signup2 extends Activity {
    Spinner major;
    ProgressDialog mProgressDialog;

    StorageReference mStorage;
    DatabaseReference mRef;

    boolean hostelite = false;

    private static final int GALLERY_INTENT =  2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);

        Intent intent = getIntent();
        String sc = intent.getStringExtra("nustor.orientationassistant.SCHOOL");

        major = (Spinner) findViewById(R.id.major);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                getArray(sc), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        major.setAdapter(adapter);

        mStorage = FirebaseStorage.getInstance().getReference();

        mProgressDialog = new ProgressDialog(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
            mProgressDialog.setMessage("Uploading Image...");
            mProgressDialog.show();
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);

            Uri uri = data.getData();

            MyApplication mApp = (MyApplication)getApplicationContext();

            StorageReference filepath = mStorage.child("ProfilePics").child(mApp.getLoggedInId());

            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(signup2.this,"Photo has been uploaded",Toast.LENGTH_LONG).show();
                    mProgressDialog.dismiss();

                }
            });


        }
    }

    public int getArray(String school){
        switch(school){
            case "SEECS":
                return R.array.SEECSc;
            case "SCEE":
                return R.array.SCEEc;
            case "SCME":
                return R.array.SCMEc;
            case "SMME":
                return R.array.SMMEc;
            case "NBS":
                return R.array.NBSc;
            case "SADA":
                return R.array.SADAc;
            case "ASAB":
                return R.array.ASABc;
            case "S3H":
                return R.array.S3Hc;
            case "SNS":
                return R.array.SNSc;
            default:
                return R.array.SEECSc;
        }
    }

    public void hostelSelection(View view){
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.hostelite:
                if (checked)
                    hostelite = true;
                    break;
            case R.id.dayScholar:
                if (checked)
                    hostelite = false;
                    break;
        }
    }

    public void goToGallery(View view){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,GALLERY_INTENT);
    }

    public void sendToFirebase(View view){

        MyApplication mApp = (MyApplication)getApplicationContext();

        String major = ((Spinner) findViewById(R.id.major)).getSelectedItem().toString();
        mRef = FirebaseDatabase.getInstance().getReference().child("users").child("data").child(mApp.getLoggedInId()).child("major");
        mRef.setValue(major);

        String desc = ((EditText) findViewById(R.id.description)).getText().toString();
        mRef = FirebaseDatabase.getInstance().getReference().child("users").child("data").child(mApp.getLoggedInId()).child("description");
        mRef.setValue(desc);

        mRef = FirebaseDatabase.getInstance().getReference().child("users").child("data").child(mApp.getLoggedInId()).child("hostel");
        mRef.setValue(hostelite);

        nextStep();
    }

    public void nextStep(){
        Intent intent = new Intent(this, home.class);
        startActivity(intent);
    }


}
