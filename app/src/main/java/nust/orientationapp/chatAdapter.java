package nust.orientationapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Talha on 9/22/16.
 */
public class chatAdapter extends ArrayAdapter<Message> {
    private static final int MY_MESSAGE = 0, OTHER_MESSAGE = 1;
    private String myUID;

    public chatAdapter(Context context, ArrayList<Message> data, String myUID) {
        super(context, R.layout.item_mine_message, data);
        this.myUID = myUID;
    }

    @Override
    public int getViewTypeCount() {
        // my message, other message, my image, other image
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        Message item = getItem(position);

        if (item.getSenderUID().equals(myUID)) {
            return MY_MESSAGE;
        } else {
            return OTHER_MESSAGE;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position);
        if (viewType == MY_MESSAGE) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_mine_message, parent, false);

            TextView message = (TextView) convertView.findViewById(R.id.text);
            message.setText(getItem(position).getMessage());

            TextView dateTime = (TextView) convertView.findViewById(R.id.dateTime);
            dateTime.setText(getDateTime(getItem(position).getTimeStamp()));

        } else if (viewType == OTHER_MESSAGE) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_other_message, parent, false);

            TextView textView = (TextView) convertView.findViewById(R.id.text);
            textView.setText(getItem(position).getMessage());

            TextView dateTime = (TextView) convertView.findViewById(R.id.dateTime);
            dateTime.setText(getDateTime(getItem(position).getTimeStamp()));

        }

        convertView.findViewById(R.id.chatMessageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), "onClick", Toast.LENGTH_LONG).show();
            }
        });


        return convertView;

    }

    public String getDateTime(String timeStamp){
        String currentTime = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        String currentDate = (currentTime.split("_"))[0];

        String messageDate = (timeStamp.split("_"))[0];

        if(currentDate.equals(messageDate)){
            return timeStamp.substring(9,11) + ":" + timeStamp.substring(11,13);
        }else{
            return messageDate.substring(6,8) + "-" + messageDate.substring(4,6) + "-" + messageDate.substring(0,4);
        }
    }

}