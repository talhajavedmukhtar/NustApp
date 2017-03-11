package nust.orientationapp;

import android.content.Context;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link connectionsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link connectionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class connectionsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "title";
    private static final String ARG_PARAM2 = "page";

    // TODO: Rename and change types of parameters
    private String title;
    private int page;

    private Button goToSearchResults;

    ArrayList<Person> requestsArray = new ArrayList<>();
    ArrayList<Person> connectionsArray = new ArrayList<>();
    MyApplication mApp;

    ArrayList<String> toRemove = new ArrayList<>();

    private OnFragmentInteractionListener mListener;

    public connectionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param title Parameter 1.
     * @param page Parameter 2.
     * @return A new instance of fragment connectionsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static connectionsFragment newInstance(String title, int page) {
        connectionsFragment fragment = new connectionsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, title);
        args.putInt(ARG_PARAM2, page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_PARAM1);
            page = getArguments().getInt(ARG_PARAM2);
        }

        mApp = (MyApplication) getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_connections, container, false);


        ListView connections = (ListView) view.findViewById(R.id.connections);
        final connectionsAdapter cAdapter = new connectionsAdapter(getActivity(),connectionsArray);
        connections.setAdapter(cAdapter);

        FirebaseDatabase.getInstance().getReference().child("users").child("connections").child(mApp.getLoggedInId()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Person person = dataSnapshot.getValue(Person.class);
                connectionsArray.add(person);
                toRemove.add(person.getUid());
                cAdapter.notifyDataSetChanged();
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

        goToSearchResults = (Button)view.findViewById(R.id.goToSearchResults);
        goToSearchResults.getBackground().setColorFilter(getResources().getColor(R.color.nustDark), PorterDuff.Mode.MULTIPLY);
        goToSearchResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                        .beginTransaction();
                transaction.replace(R.id.root_frame, searchResultsFragment.newInstance(toRemove));
                transaction.commit();
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
