package nust.orientationapp;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class home extends FragmentActivity implements addFragment.OnFragmentInteractionListener,connectionsFragment.OnFragmentInteractionListener,searchResultsFragment.OnFragmentInteractionListener,mapFragment.OnFragmentInteractionListener,messagesFragment.OnFragmentInteractionListener,conversationFragment.OnFragmentInteractionListener,requestsFragment.OnFragmentInteractionListener{
    homeTabsAdapter adapterViewPager;
    ViewPager viewPager;

    private MyApplication mApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        viewPager = (ViewPager) findViewById(R.id.vPager);
        adapterViewPager = new homeTabsAdapter(getSupportFragmentManager());
        addFragments();
        viewPager.setAdapter(adapterViewPager);

        mApp = (MyApplication) getApplicationContext();

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(viewPager);

        /*email = (EditText) findViewById(R.id.email);
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });*/
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        return;
    }

    private void addFragments(){
        adapterViewPager.addFragment(new requestsFragment(), "Requests");
        adapterViewPager.addFragment(new RootFragment(), "Connections");
        adapterViewPager.addFragment(new RootFragment2(), "Messages");
        adapterViewPager.addFragment(new mapFragment(), "Map");
        adapterViewPager.addFragment(new addFragment(), "Create");

    }

    public void addEvent(View view){
        Intent intent = new Intent(home.this,addEventActivity.class);
        startActivity(intent);
    }

    public void addInterest(View view){
        Intent intent = new Intent(home.this,addInterestActivity.class);
        startActivity(intent);
    }

    /*public void goToSearchResults(View view){
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.root_frame, new searchResultsFragment());
        transaction.commit();
    }*/

    public void goToConnections(View view){
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.root_frame, new connectionsFragment());
        transaction.commit();
    }

    /*public void narrowResults(View view){
        Intent intent = new Intent(this,narrowResults.class);
        startActivityForResult(intent, 333);
    }*/

    public void goToMessages(View view){
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.root_frame2, new messagesFragment());
        transaction.commit();
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 333){
            if(resultCode == 33){

                searchResultsFragment srFragment = new searchResultsFragment();
                srFragment.setArguments(data.getExtras());

                FragmentTransaction transaction = getSupportFragmentManager()
                        .beginTransaction();
                transaction.replace(R.id.root_frame, srFragment);
                transaction.commit();

            }
        }

    }*/

    public void setViewPagerItem(int i){
        try{
            viewPager.setCurrentItem(i);
        }catch (Exception e){

        }
    }

    @Override
    public void onBackPressed() {
        if(viewPager.getCurrentItem() == 2){
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction();
            transaction.replace(R.id.root_frame2, new messagesFragment());
            transaction.commit();
        }
        if(viewPager.getCurrentItem() == 1){
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction();
            transaction.replace(R.id.root_frame, new connectionsFragment());
            transaction.commit();
        }
        return;
    }
}
