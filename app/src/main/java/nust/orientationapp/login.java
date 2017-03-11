package nust.orientationapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class login extends Activity {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthListener;

    private Person person;

    private Button login;
    private Button register;
    private ImageView nustLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("login", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("login", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        login = (Button) findViewById(R.id.login);
        login.getBackground().setColorFilter(getResources().getColor(R.color.nustDark), PorterDuff.Mode.MULTIPLY);

        register = (Button) findViewById(R.id.register);
        register.getBackground().setColorFilter(getResources().getColor(R.color.nustLight), PorterDuff.Mode.MULTIPLY);

        nustLogo = (ImageView) findViewById(R.id.nustLogo);
        Picasso.with(this).load(R.drawable.nustlogo).into(nustLogo);

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

    public void signIn(View view){
        String email = ((EditText) findViewById(R.id.email)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();

        if(email.equals("") || password.equals("")){
            Toast.makeText(this,"Please enter an email and a password",Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("login", "signInWithEmail:onComplete:" + task.isSuccessful());

                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if(task.isSuccessful()){
                    final FirebaseUser user = mAuth.getCurrentUser();

                    Toast.makeText(login.this, "Authentication successful",
                            Toast.LENGTH_SHORT).show();

                    FirebaseDatabase.getInstance().getReference().child("users").child("data").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            MyApplication mApp = (MyApplication)getApplicationContext();
                            mApp.setLoggedInId(user.getUid());

                            person = dataSnapshot.getValue(Person.class);
                            mApp.setLoggedInPerson(person);

                            Intent intent = new Intent(login.this,home.class);
                            startActivity(intent);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });



                }
                if (!task.isSuccessful()) {
                    Log.w("login", "signInWithEmail:failed", task.getException());
                    Toast.makeText(login.this, "Cannot Sign In. Please recheck email and password.",
                            Toast.LENGTH_SHORT).show();
                }

                // ...
            }
        });
    }


    public void goToSignUp(View view){
        Intent intent = new Intent(this,signup.class);
        startActivity(intent);
    }
}
