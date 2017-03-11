package nust.orientationapp;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Talha on 9/15/16.
 */
public class Message {
    String message;
    String senderUID;
    String timeStamp;

    public Message(){}

    public Message(String message, String senderUID) {
        this.message = message;
        this.senderUID = senderUID;
        this.timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderUID() {
        return senderUID;
    }

    public void setSenderUID(String senderUID) {
        this.senderUID = senderUID;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
