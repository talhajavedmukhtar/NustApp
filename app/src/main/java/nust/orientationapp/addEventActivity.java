package nust.orientationapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class addEventActivity extends FragmentActivity {
    private Button setPlace;
    private Button save;

    private EditText eventDate;
    private EditText eventStartTime;
    private EditText eventEndTime;

    private LatLng eventLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        setPlace = (Button) findViewById(R.id.setPlace);
        setPlace.getBackground().setColorFilter(getResources().getColor(R.color.nustLight), PorterDuff.Mode.MULTIPLY);

        save = (Button) findViewById(R.id.save);
        save.getBackground().setColorFilter(getResources().getColor(R.color.nustDark), PorterDuff.Mode.MULTIPLY);

        eventDate = (EditText) findViewById(R.id.eventDate);
        eventStartTime = (EditText) findViewById(R.id.eventStartTime);
        eventEndTime = (EditText) findViewById(R.id.eventEndTime);

    }

    public void saveEvent(View view){
        MyApplication mApp = (MyApplication) getApplicationContext();
        String creatorId = mApp.getLoggedInId();

        if(eventLocation == null){
            Toast.makeText(this,"Please select a location",Toast.LENGTH_LONG).show();
        }else {
            String name = ((EditText)findViewById(R.id.eventName)).getText().toString();
            String place = ((EditText)findViewById(R.id.eventPlace)).getText().toString();
            String date = ((EditText)findViewById(R.id.eventDate)).getText().toString();
            String startTime = ((EditText)findViewById(R.id.eventStartTime)).getText().toString();
            String endTime = ((EditText)findViewById(R.id.eventEndTime)).getText().toString();
            Event createdEvent = new Event(creatorId,name,place,date,startTime,endTime,eventLocation.latitude,eventLocation.longitude);

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("events").push();
            ref.setValue(createdEvent);

            finish();
        }

    }

    public void setPlace(View view){
        Intent intent = new Intent(this,selectLocation.class);
        startActivityForResult(intent,333);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 333){
            if(resultCode == Activity.RESULT_OK){
                eventLocation = data.getParcelableExtra("eventLocation");
            }
        }
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        private EditText eventTime;

        public static TimePickerFragment newInstance(EditText eT){
            TimePickerFragment fragment = new TimePickerFragment();
            fragment.eventTime = eT;
            return fragment;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            String time = Integer.toString(hourOfDay) + Integer.toString(minute) + "00";
            eventTime.setText(time);

        }
    }

    public void showStartTimePickerDialog(View v) {
        DialogFragment newFragment = TimePickerFragment.newInstance(eventStartTime);
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public void showEndTimePickerDialog(View v){
        DialogFragment newFragment = TimePickerFragment.newInstance(eventEndTime);
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        private EditText eventDate;

        public static DatePickerFragment newInstance(EditText eD){
            DatePickerFragment fragment = new DatePickerFragment();
            fragment.eventDate = eD;
            return fragment;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            String date;

            if(month <= 8) {
                date = Integer.toString(year) + "0" + Integer.toString(month + 1) + Integer.toString(day);
            }else{
                date = Integer.toString(year) + Integer.toString(month + 1) + Integer.toString(day);
            }
            eventDate.setText(date);
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = DatePickerFragment.newInstance(eventDate);
        newFragment.show(getFragmentManager(), "datePicker");
    }
}
