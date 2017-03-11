package nust.orientationapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Talha on 9/11/16.
 */
public class selectLocation extends FragmentActivity implements OnMapReadyCallback {
    private LatLng eventLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        LatLngBounds NUST = new LatLngBounds(
                new LatLng(33.632062, 72.978434),new LatLng(33.658932,73.003921));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(NUST.getCenter(), 16));
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                googleMap.clear();
                googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("Set Event Location at " + latLng.latitude + ", " + latLng.longitude));
                eventLocation = latLng;
            }
        });
    }

    public void saveLocation(View view){
        if(eventLocation == null){
            Toast.makeText(this,"Please select a location",Toast.LENGTH_LONG).show();
        }else {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("eventLocation",(Parcelable) eventLocation);
            setResult(Activity.RESULT_OK,returnIntent);
            finish();
        }
    }

}
