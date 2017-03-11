package nust.orientationapp;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signup extends Activity {
    private Spinner profileType;
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText password;
    private Spinner school;
    private EditText homeTown;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference ref;

    public final static String EXTRA_MESSAGE = "nustor.orientationassistant.SCHOOL";

    MyApplication mApp;

    private Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //setting up the profile type spinner
        profileType = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.profileTypes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        profileType.setAdapter(adapter);

        //setting up the school
        school = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.schools, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        school.setAdapter(adapter2);

        firstName = (EditText)findViewById(R.id.first);
        lastName = (EditText) findViewById(R.id.last);
        email = (EditText) findViewById(R.id.emailInput);
        password = (EditText) findViewById(R.id.pwInput);
        homeTown = (EditText) findViewById(R.id.homeTown);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("signup", "onAuthStateChanged:signed_in:" + user.getUid());

                } else {
                    // User is signed out
                    Log.d("signup", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void sendToFirebase(View view){
        firstName = (EditText)findViewById(R.id.first);
        lastName = (EditText) findViewById(R.id.last);
        email = (EditText) findViewById(R.id.emailInput);
        password = (EditText) findViewById(R.id.pwInput);
        homeTown = (EditText) findViewById(R.id.homeTown);
        profileType = (Spinner) findViewById(R.id.spinner);
        school = (Spinner) findViewById(R.id.spinner2);

        String fn = firstName.getText().toString();
        String ln = lastName.getText().toString();
        String pt = profileType.getSelectedItem().toString();
        String em = email.getText().toString();
        String pw = password.getText().toString();
        String sc = school.getSelectedItem().toString();
        String ht = homeTown.getText().toString();

        if(em.equals("") || pw.equals("")){
            Toast.makeText(this,"Please enter a valid email and password",Toast.LENGTH_LONG).show();
            return;
        }

        if(fn.equals("") || ln.equals("") || pt == null || sc == null || ht.equals("")){
            Toast.makeText(this,"Please fill in all the fields",Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(em, pw)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("signup", "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if(task.isSuccessful()){
                            Toast.makeText(signup.this,"Sign Up Successful!",Toast.LENGTH_LONG).show();
                            FirebaseUser user = mAuth.getCurrentUser();

                            mApp = (MyApplication)getApplicationContext();
                            mApp.setLoggedInId(user.getUid());

                            saveData(user.getUid());
                        }
                        if (!task.isSuccessful()) {
                            Toast.makeText(signup.this, task.getException().toString(),
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

    }

    public void saveData(String id){
        firstName = (EditText)findViewById(R.id.first);
        lastName = (EditText) findViewById(R.id.last);
        email = (EditText) findViewById(R.id.emailInput);
        password = (EditText) findViewById(R.id.pwInput);
        homeTown = (EditText) findViewById(R.id.homeTown);
        profileType = (Spinner) findViewById(R.id.spinner);
        school = (Spinner) findViewById(R.id.spinner2);

        ref = FirebaseDatabase.getInstance().getReference().child("users").child("data").child(id);

        String fn = firstName.getText().toString();
        String ln = lastName.getText().toString();
        String pt = profileType.getSelectedItem().toString();
        String em = email.getText().toString();
        String pw = password.getText().toString();
        String sc = school.getSelectedItem().toString();
        String ht = homeTown.getText().toString();

        person = new Person(fn,ln,em,pw,sc,ht,pt,id);

        ref.setValue(person);

        FirebaseDatabase.getInstance().getReference().child("homeTowns").child(ht).setValue("0");

        nextStep(sc);
    }

    public void nextStep(String school){
        Intent intent = new Intent(this, signup2.class);

        MyApplication mApp = (MyApplication) getApplicationContext();
        mApp.setLoggedInPerson(person);

        intent.putExtra(EXTRA_MESSAGE,school);
        startActivity(intent);
    }
}
