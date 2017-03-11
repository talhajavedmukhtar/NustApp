package nust.orientationapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link searchResultsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link searchResultsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class searchResultsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "school";
    private static final String ARG_PARAM2 = "homeTown";
    private static final String ARG_PARAM3 = "hostelite";

    // TODO: Rename and change types of parameters
    private String school;
    private String homeTown;
    private boolean hostelite;
    private String interestId;

    private boolean bySchool = false;
    private boolean byHomeTown = false;
    private boolean byHostelite = false;
    private boolean byInterest = false;

    private ListView searchResults;
    private Button narrowResultsButton;

    private ArrayList<String> toRemove;

    private ArrayList<Person> results;
    private searchResultsAdapter sRAdapter;

    private MyApplication mApp;

    private OnFragmentInteractionListener mListener;

    public searchResultsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static searchResultsFragment newInstance(String school, String homeTown, Boolean hostelite) {
        searchResultsFragment fragment = new searchResultsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, school);
        args.putString(ARG_PARAM2, homeTown);
        args.putBoolean(ARG_PARAM3, hostelite);
        fragment.setArguments(args);
        return fragment;
    }

    public static searchResultsFragment newInstance(ArrayList<String> toRemove){
        searchResultsFragment fragment = new searchResultsFragment();
        Bundle args = new Bundle();
        args.putStringArrayList("toRemove",toRemove);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if(getArguments().containsKey("school")) {
                school = getArguments().getString(ARG_PARAM1);
                bySchool = true;
            }
            if(getArguments().containsKey("homeTown")) {
                homeTown = getArguments().getString(ARG_PARAM2);
                byHomeTown = true;
            }
            if(getArguments().containsKey("hostelite")) {
                hostelite = getArguments().getBoolean(ARG_PARAM3);
                byHostelite = true;
            }
            if(getArguments().containsKey("interest")) {
                interestId = getArguments().getString("interest");
                byInterest = true;
            }
            if(getArguments().containsKey("toRemove")){
                mApp = (MyApplication) getActivity().getApplicationContext();

                toRemove = getArguments().getStringArrayList("toRemove");
                toRemove.add(mApp.getLoggedInId());
            }

        }



        /*DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
        ref.orderByChild("email").equalTo(email).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Person person = dataSnapshot.getValue(Person.class);
                results.add(person);
                sRAdapter.notifyDataSetChanged();
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
        });*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_search_results, container, false);

        results = new ArrayList<Person>();
        sRAdapter = new searchResultsAdapter(results,getContext());

        searchResults = (ListView) view.findViewById(R.id.searchResults1);
        searchResults.setAdapter(sRAdapter);

        Query narrowResults = queryBuilder(bySchool,byHomeTown,byHostelite,byInterest);

        Log.d("newresultquery",narrowResults.toString());

        if(!byInterest){
            narrowResults.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Person person = dataSnapshot.getValue(Person.class);
                    Log.d("newresult",person.getEmail());
                    if(toRemove != null){
                        if(toRemove.contains(person.getUid())){
                            return;
                        }
                    }
                    results.add(person);
                    sRAdapter.notifyDataSetChanged();
                    searchResults.setAdapter(sRAdapter);
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
        }else{
            narrowResults.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    String uid = dataSnapshot.getValue(String.class);
                    Log.d("srFragment",uid);
                    if(toRemove != null){
                        if(toRemove.contains(uid)){
                            return;
                        }
                    }
                    FirebaseDatabase.getInstance().getReference().child("users").child("data").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Person person = dataSnapshot.getValue(Person.class);
                            results.add(person);
                            Log.d("newresult",person.getEmail());
                            sRAdapter.notifyDataSetChanged();
                            searchResults.setAdapter(sRAdapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
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
        }

        narrowResultsButton = (Button)view.findViewById(R.id.narrowResults);
        narrowResultsButton.getBackground().setColorFilter(getActivity().getResources().getColor(R.color.nustLight), PorterDuff.Mode.MULTIPLY);
        narrowResultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),narrowResults.class);
                intent.putStringArrayListExtra("toRemove",toRemove);
                startActivityForResult(intent, 333);
            }
        });


        return view;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private Query queryBuilder(boolean s,boolean hT,boolean h,boolean i){
        Query ref = FirebaseDatabase.getInstance().getReference().child("users").child("data");

        if(s)
            ref = ref.orderByChild("school").equalTo(school);

        if(hT)
            ref = ref.orderByChild("homeTown").equalTo(homeTown);

        if(h)
            ref = ref.orderByChild("hostel").equalTo(hostelite);

        if(i)
            return FirebaseDatabase.getInstance().getReference().child("users").child("interests").child(interestId);

        return ref;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 333){
            if(resultCode == 33){

                searchResultsFragment srFragment = searchResultsFragment.newInstance(toRemove);
                srFragment.setArguments(data.getExtras());

                FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                        .beginTransaction();
                transaction.replace(R.id.root_frame, srFragment);
                transaction.commit();

            }
        }
    }
}
