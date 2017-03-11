package nust.orientationapp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Talha on 9/8/16.
 */
public class connectionsAdapter extends ArrayAdapter<Person> {
    private ArrayList<Person> persons;
    private Context context;
    LayoutInflater inflater;

    private Button removeConnection;
    private Button viewProfile;
    private Button message;

    private StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://orientationapp-328eb.appspot.com");

    public connectionsAdapter(Context context, ArrayList<Person> persons) {
        super(context, R.layout.connection_item,persons);
        this.persons = persons;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return persons.size();
    }

    @Override
    public Person getItem(int i) {
        return persons.get(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.connection_item,null);
        final Person person = persons.get(i);

        final MyApplication mApp = (MyApplication) context.getApplicationContext();

        TextView name = (TextView)view.findViewById(R.id.name);
        TextView school = (TextView)view.findViewById(R.id.school);
        TextView profileType = (TextView)view.findViewById(R.id.profileType);
        final ImageView profilePicture = (ImageView)view.findViewById(R.id.profilePicture);
        profilePicture.setVisibility(View.VISIBLE);

        /* String url = "https://firebasestorage.googleapis.com/b/orientationapp-328eb.appspot.com/o/ProfilePictures%20" + person.getUid() + ".jpg"; */
        storageRef.child("ProfilePics").child(person.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(context).load(uri).transform(new CircleTransform()).fit().centerInside().error(R.drawable.placeholder).placeholder(R.drawable.progress_animation).into(profilePicture);
            }
        });

        name.setText(person.getFirstName()+" "+person.getLastName());
        school.setText(person.getSchool());
        profileType.setText(person.getProfileType());

        message = (Button)view.findViewById(R.id.message);
        message.getBackground().setColorFilter(context.getResources().getColor(R.color.nustLight), PorterDuff.Mode.MULTIPLY);
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String conversationId = getConversationId(mApp.getLoggedInId(),person.getUid());
                String name = person.getFirstName() + " " + person.getLastName();

                FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager()
                        .beginTransaction();
                transaction.replace(R.id.root_frame2, conversationFragment.newInstance(conversationId,name,person.getUid()));
                transaction.commit();

                home home = (home) context;
                home.setViewPagerItem(2);
            }
        });

        removeConnection = (Button) view.findViewById(R.id.removeConnection);
        removeConnection.getBackground().setColorFilter(context.getResources().getColor(R.color.nustLight), PorterDuff.Mode.MULTIPLY);
        removeConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("users").child("connections").child(mApp.getLoggedInId()).child(person.getUid()).removeValue();
                FirebaseDatabase.getInstance().getReference().child("users").child("connections").child(person.getUid()).child(mApp.getLoggedInId()).removeValue();

                persons.remove(person);
                notifyDataSetChanged();
            }
        });

        viewProfile = (Button)view.findViewById(R.id.viewProfile);
        viewProfile.getBackground().setColorFilter(context.getResources().getColor(R.color.nustLight), PorterDuff.Mode.MULTIPLY);
        viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,viewProfile.class);
                intent.putExtra("person", (Parcelable) person);
                context.startActivity(intent);
            }
        });

        return view;
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
}
