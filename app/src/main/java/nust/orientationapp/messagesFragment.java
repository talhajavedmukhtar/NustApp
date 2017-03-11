package nust.orientationapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


public class messagesFragment extends Fragment {
    private MyApplication mApp;
    private DatabaseReference connectionsRef;
    private StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://orientationapp-328eb.appspot.com");

    private OnFragmentInteractionListener mListener;

    private Context context;

    private TextView unreadCount;

    public messagesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mApp = (MyApplication) getActivity().getApplicationContext();

        connectionsRef = FirebaseDatabase.getInstance().getReference().child("users").child("connections").child(mApp.getLoggedInId());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        ListView connections = (ListView) view.findViewById(R.id.connections);
        ListAdapter connectionsAdapter = new FirebaseListAdapter<Person>(getActivity(),Person.class,R.layout.connection_conversation_item,connectionsRef) {
            @Override
            protected void populateView(View v, final Person model, int position) {
                TextView name = (TextView) v.findViewById(R.id.name);
                //TextView school = (TextView) v.findViewById(R.id.school);
                //TextView profileType = (TextView) v.findViewById(R.id.profileType);
                final ImageView profilePicture = (ImageView)v.findViewById(R.id.profilePicture);
                profilePicture.setVisibility(View.VISIBLE);

                name.setText(model.getFirstName() + " " + model.getLastName());
                //school.setText(model.getSchool());
                //profileType.setText(model.getProfileType());

                unreadCount = (TextView)v.findViewById(R.id.unreadCount);
                updateUnreadCount(String.valueOf(model.getUnread()));

                storageRef.child("ProfilePics").child(model.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.with(context).load(uri).transform(new CircleTransform()).fit().centerInside().error(R.drawable.placeholder).placeholder(R.drawable.progress_animation).into(profilePicture);
                    }
                });

                v.setOnClickListener(new View.OnClickListener() {
                    String conversationId = getConversationId(mApp.getLoggedInId(),model.getUid());

                    @Override
                    public void onClick(View view) {
                        String name = model.getFirstName() + " " + model.getLastName();

                        FragmentTransaction transaction = getFragmentManager()
                                .beginTransaction();
                        transaction.replace(R.id.root_frame2, conversationFragment.newInstance(conversationId,name,model.getUid()));
                        transaction.commit();

                        FirebaseDatabase.getInstance().getReference().child("users").child("connections").child(mApp.getLoggedInId()).child(model.getUid()).child("unread").setValue(0);

                        Log.d("conversationId",conversationId);
                    }
                });

                FirebaseDatabase.getInstance().getReference().child("users").child("connections").child(mApp.getLoggedInId()).child(model.getUid()).child("unread").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String unread = String.valueOf(dataSnapshot.getValue(int.class));
                        updateUnreadCount(unread);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };

        connections.setAdapter(connectionsAdapter);

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

        this.context = context;
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

    public String getConversationId(final String senderID, final String receiverID){
        int senderSum = 0;
        int receiverSum = 0;

        for (int i = 0; i < senderID.length(); i++){
            senderSum += Character.getNumericValue(senderID.charAt(i));
        }

        for (int i = 0; i < receiverID.length(); i++){
            receiverSum += Character.getNumericValue(receiverID.charAt(i));
        }

        if(senderSum > receiverSum){
            return senderID + receiverID;
        }else {
            return receiverID + senderID;
        }
    }

    public void updateUnreadCount(String value){
        if(value == null){
            unreadCount.setText("0");
        }else{
            unreadCount.setText(value);
        }
    }
}
