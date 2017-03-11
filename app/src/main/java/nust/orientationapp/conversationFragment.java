package nust.orientationapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class conversationFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    private String conversationId;
    private String personName;
    private String personUid;

    private ImageView profilePicture;
    private TextView name;

    private EditText messageET;
    private Button sendMessage;
    private ListView messages;

    private int initiallyUnread = -1;
    private int newlyUnread = -1;

    private String loggedInId;
    private String otherPersonId;

    private MyApplication mApp;

    private ProgressDialog mProgressDialog;

    private ArrayList<Message> conversationMessages = new ArrayList<>();

    private DatabaseReference messagesRef;

    private StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://orientationapp-328eb.appspot.com");

    public conversationFragment() {
        // Required empty public constructor
    }

    public static conversationFragment newInstance(String conversationId, String personName, String personUid) {
        conversationFragment fragment = new conversationFragment();
        Bundle args = new Bundle();
        args.putString("conversationId", conversationId);
        args.putString("name",personName);
        args.putString("uid",personUid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            conversationId = getArguments().getString("conversationId");
            personName = getArguments().getString("name");
            personUid = getArguments().getString("uid");
        }

        mApp = (MyApplication) getActivity().getApplicationContext();
        mProgressDialog = new ProgressDialog(getActivity());
        messagesRef = FirebaseDatabase.getInstance().getReference().child("conversations").child(conversationId);

        loggedInId = mApp.getLoggedInId();
        otherPersonId = conversationId.replace(loggedInId,"");
        Log.d("opid",otherPersonId);

        FirebaseDatabase.getInstance().getReference().child("users").child("connections").child(otherPersonId).child(loggedInId).child("unread").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    initiallyUnread = (int) dataSnapshot.getValue();
                }catch (ClassCastException c){
                    initiallyUnread = (int)((long) dataSnapshot.getValue());
                }

                Log.d("here","1");
                if(newlyUnread != -1){
                    FirebaseDatabase.getInstance().getReference().child("users").child("connections").child(otherPersonId).child(loggedInId).child("unread").setValue(initiallyUnread + newlyUnread);
                    Log.d("here","2");
                }
                Log.d("here","3");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_conversation, container, false);
        messageET = (EditText) view.findViewById(R.id.newMessage);
        sendMessage = (Button) view.findViewById(R.id.sendMessage);
        messages = (ListView) view.findViewById(R.id.messages);

        /*ListAdapter messagesAdapter = new FirebaseListAdapter<Message>(getActivity(),Message.class,R.layout.message_item,messagesRef) {
            @Override
            protected void populateView(View v, Message model, int position) {
                TextView messageText = (TextView) v.findViewById(R.id.messageText);
                messageText.setTextColor(getResources().getColor(R.color.white));
                TextView dateTime = (TextView) v.findViewById(R.id.timeDate);
                dateTime.setTextColor(getResources().getColor(R.color.white));

                if(model.getSenderUID().equals(mApp.getLoggedInId())){
                    v.setBackgroundColor(getResources().getColor(R.color.nustDark));
                }else{
                    v.setBackgroundColor(getResources().getColor(R.color.nustLight));
                }

                messageText.setText(model.getMessage());
                dateTime.setText(getDateTime(model.getTimeStamp()));
            }
        };*/

        final chatAdapter messagesAdapter = new chatAdapter(getActivity(),conversationMessages,mApp.getLoggedInId());

        FirebaseDatabase.getInstance().getReference().child("conversations").child(conversationId).limitToLast(20).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message msg = dataSnapshot.getValue(Message.class);
                conversationMessages.add(msg);
                messagesAdapter.notifyDataSetChanged();
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

        messages.setAdapter(messagesAdapter);

        sendMessage.getBackground().setColorFilter(getResources().getColor(R.color.nustDark), PorterDuff.Mode.MULTIPLY);
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String conversationId = getConversationId();

                final String message = getMessage();
                String senderUID = mApp.getLoggedInId();

                Message toSend = new Message(message,senderUID);

                mProgressDialog.setMessage("Sending Message...");
                mProgressDialog.show();
                mProgressDialog.setCancelable(false);
                mProgressDialog.setCanceledOnTouchOutside(false);

                FirebaseDatabase.getInstance().getReference().child("conversations").child(conversationId).push().setValue(toSend).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mProgressDialog.dismiss();
                        messageET.setText("");

                        if(newlyUnread == -1){
                            newlyUnread += 2;
                        }else {
                            newlyUnread += 1;
                        }

                        Log.d("here","4");
                        if(initiallyUnread != -1){
                            FirebaseDatabase.getInstance().getReference().child("users").child("connections").child(otherPersonId).child(loggedInId).child("unread").setValue(initiallyUnread + newlyUnread);
                            Log.d("here","5");
                        }
                        Log.d("here","6");

                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mProgressDialog.dismiss();
                        Toast.makeText(getActivity(),"Could not send message. Try again.",Toast.LENGTH_LONG).show();
                    }
                });
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

    public String getConversationId(){
        return conversationId;
    }

    public String getMessage(){
        return messageET.getText().toString();
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
