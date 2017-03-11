package nust.orientationapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class narrowResults extends Activity implements searchResultsFragment.OnFragmentInteractionListener{
    private Spinner school;
    private Spinner homeTown;
    private Spinner hostelite;
    private Spinner interest;

    List<String> homeTowns = new ArrayList<>();
    List<String> interests = new ArrayList<>();
    List<String> interestIds = new ArrayList<>();

    ArrayList<String> toRemove;

    ArrayAdapter<String> hTAdapter;
    ArrayAdapter<String> iAdapter;

    private boolean schoolFlag = false;
    private boolean homeTownFlag = false;
    private boolean hosteliteFlag = false;
    private boolean interestFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_narrow_results);

        Intent received = getIntent();
        toRemove = received.getStringArrayListExtra("toRemove");

        homeTowns.add("No preference");

        FirebaseDatabase.getInstance().getReference().child("homeTowns").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String newHT = dataSnapshot.getKey();
                homeTowns.add(newHT);
                hTAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        homeTown = (Spinner)findViewById(R.id.homeTown);
        hTAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,homeTowns);
        hTAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        homeTown.setAdapter(hTAdapter);

        interests.add("No preference");
        interestIds.add("0");

        FirebaseDatabase.getInstance().getReference().child("interestBeacons").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                InterestBeacon newInterest = dataSnapshot.getValue(InterestBeacon.class);
                interests.add(newInterest.getInterestName());
                interestIds.add(newInterest.getInterestId());
                iAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        interest = (Spinner)findViewById(R.id.interests);
        iAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,interests);
        iAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        interest.setAdapter(iAdapter);


        school = (Spinner) findViewById(R.id.schoolSelect);
        final ArrayAdapter<CharSequence> schoolAdapter = ArrayAdapter.createFromResource(this,
                R.array.schoolsSearch, android.R.layout.simple_spinner_item);
        schoolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        school.setAdapter(schoolAdapter);

        hostelite = (Spinner) findViewById(R.id.hostelite);
        final ArrayAdapter<CharSequence> hosteliteAdapter = ArrayAdapter.createFromResource(this,
                R.array.hostelite, android.R.layout.simple_spinner_item);
        hosteliteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hostelite.setAdapter(hosteliteAdapter);

        school.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                schoolFlag = true;
                return false;
            }
        });

        homeTown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                homeTownFlag = true;
                return false;
            }
        });

        hostelite.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hosteliteFlag = true;
                return false;
            }
        });

        interest.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                interestFlag = true;
                return false;
            }
        });


        school.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(schoolFlag){
                    if(schoolAdapter.getItem(i) == "No Preference"){
                        finish();
                    }
                    Intent result = new Intent();
                    String school = (String)schoolAdapter.getItem(i);
                    result.putExtra("school", school);
                    result.putStringArrayListExtra("toRemove",toRemove);
                    setResult(33,result);
                    finish();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        homeTown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(homeTownFlag){
                    if(hTAdapter.getItem(i) == "No Preference"){
                        finish();
                    }

                    Intent result = new Intent();
                    String hT = (String)hTAdapter.getItem(i);
                    result.putExtra("homeTown", hT);
                    result.putStringArrayListExtra("toRemove",toRemove);
                    setResult(33,result);
                    finish();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        hostelite.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(hosteliteFlag){
                    if(hosteliteAdapter.getItem(i) == "No Preference"){
                        finish();
                    }

                    Intent result = new Intent();

                    Log.d("narrowSelected",hosteliteAdapter.getItem(i).toString());

                    if(hosteliteAdapter.getItem(i).toString().equals("Hostelite")){
                        result.putExtra("hostelite", true);
                        Log.d("narrowResults","true");
                    }else{
                        result.putExtra("hostelite", false);
                        Log.d("narrowResults","false");
                    }

                    result.putStringArrayListExtra("toRemove",toRemove);
                    setResult(33,result);
                    finish();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

        interest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(interestFlag){
                    if(iAdapter.getItem(i) == "No Preference"){
                        finish();
                    }

                    Intent result = new Intent();
                    String interest = interestIds.get(i);
                    result.putExtra("interest", interest);
                    result.putStringArrayListExtra("toRemove",toRemove);
                    setResult(33,result);
                    finish();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    /*public void goBack(View view){
        Bundle bundle = new Bundle();

        if(school.getSelectedItem() != null || school.getSelectedItem().toString() != "No Preference")
            bundle.putString("school",school.getSelectedItem().toString());

        if(homeTown.getSelectedItem() != null || homeTown.getSelectedItem().toString() != "No Preference")
            bundle.putString("homeTown",homeTown.getSelectedItem().toString());

        if(hostelite.getSelectedItem() != null || hostelite.getSelectedItem().toString() != "No Preference"){
            if(hostelite.getSelectedItem().toString() == "hostelite"){
                bundle.putBoolean("hostelite",true);
            }else{
                bundle.putBoolean("hostelite",false);
            }
        }

        searchResultsFragment srFragment = new searchResultsFragment();
        srFragment.setArguments(bundle);

        home hm = (home)getParent();

        FragmentTransaction transaction = hm.getSupportFragmentManager()
                .beginTransaction();

        transaction.replace(R.id.root_frame, srFragment);
        transaction.commit();

        finish();
    }*/

    @Override
    public void onFragmentInteraction(Uri uri) {
        return;
    }
}
