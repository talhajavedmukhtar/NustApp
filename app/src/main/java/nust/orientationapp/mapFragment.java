package nust.orientationapp;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link mapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link mapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class mapFragment extends Fragment implements OnMapReadyCallback{
    private String title;
    private int page;

    private GoogleMap gMap;

    private View view;
    private Button plotEvents;
    private Button reposition;
    private Button plotPlaces;

    private DatabaseReference eventsRef;

    private OnFragmentInteractionListener mListener;

    private  LatLngBounds NUST = new LatLngBounds(
            new LatLng(33.632062, 72.978434),new LatLng(33.658932,73.003921));

    public mapFragment() {
        // Required empty public constructor
    }

    public static mapFragment newInstance(String title, int page) {
        mapFragment fragment = new mapFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putInt("page", page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString("title");
            page = getArguments().getInt("page");
        }

        eventsRef = FirebaseDatabase.getInstance().getReference().child("events");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        try {
            view = inflater.inflate(R.layout.fragment_map, container, false);
        }catch (InflateException e){
            return view;
        }

        plotEvents = (Button) view.findViewById(R.id.plotEvents);
        plotEvents.getBackground().setColorFilter(getActivity().getResources().getColor(R.color.nustDark), PorterDuff.Mode.MULTIPLY);
        plotEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gMap.clear();
                eventsRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Event event = dataSnapshot.getValue(Event.class);
                        try {
                            plotEvent(event);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
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
        });

        reposition = (Button)view.findViewById(R.id.reposition);
        reposition.getBackground().setColorFilter(getActivity().getResources().getColor(R.color.nustDark), PorterDuff.Mode.MULTIPLY);
        reposition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(NUST.getCenter(), 16));
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        plotPlaces = (Button) view.findViewById(R.id.plotPlaces);
        plotPlaces.getBackground().setColorFilter(getActivity().getResources().getColor(R.color.nustDark), PorterDuff.Mode.MULTIPLY);
        plotPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),selectPlaceType.class);
                startActivityForResult(intent,222);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        //googleMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(NUST.getCenter(), 16));
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

    private void plotEvent(Event event) throws ParseException {
        if(gMap != null){

            if(!eventPassed(event)){
                gMap.addMarker(new MarkerOptions()
                .position(new LatLng(event.getEventLat(),event.getEventLng()))
                .title(event.getName() + " at " + event.getPlace() + "\nDate: " + event.getDate() + "\nTime: " + event.getStartTime()));
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 222){
            if(resultCode == 22){
                Bundle bundle = data.getExtras();
                int position = bundle.getInt("position");

                Log.d("map",Integer.toString(position));

                plotPlacesByType(position);
            }
        }
    }

    private void plotPlacesByType(int pos){
        gMap.clear();

        Places places = new Places();
        String[] names = places.getNames(pos);
        LatLng[] locs = places.getLocations(pos);

        for(int i = 0; i < names.length; i++){
            gMap.addMarker(new MarkerOptions()
            .position(locs[i])
            .title(names[i]));
        }
    }

    private boolean eventPassed(Event event) throws ParseException {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss");
        String endTimeStamp = event.getDate() + "_" + event.getEndTime();
        Log.d("dates",endTimeStamp);

        String currentTime = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        Log.d("dates",currentTime);

        Date current = sdf.parse(currentTime);
        Date endTime = sdf.parse(endTimeStamp);

        Log.d("dates", Boolean.toString(current.compareTo(endTime) > 0));

        return current.compareTo(endTime) > 0;
    }
}
