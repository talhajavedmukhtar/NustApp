package nust.orientationapp;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Talha on 8/28/16.
 */
public class Event {
    private String creatorId;
    private String name;
    private String place;
    private String date;
    private String startTime;
    private String endTime;
    private double eventLat;
    private double eventLng;

    public Event(){}

    public Event(String creatorId, String name, String place, String date, String startTime, String endTime, double lat, double lng) {
        this.creatorId = creatorId;
        this.name = name;
        this.place = place;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.eventLat = lat;
        this.eventLng = lng;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public double getEventLat() {
        return eventLat;
    }

    public void setEventLat(double eventLat) {
        this.eventLat = eventLat;
    }

    public double getEventLng() {
        return eventLng;
    }

    public void setEventLng(double eventLng) {
        this.eventLng = eventLng;
    }
}
