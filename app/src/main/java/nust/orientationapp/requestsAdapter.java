package nust.orientationapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Talha on 9/8/16.
 */
public class requestsAdapter extends ArrayAdapter<Person> {
    private ArrayList<Person> persons;
    private Context context;
    LayoutInflater inflater;

    private Button viewProfile;
    private Button acceptRequest;
    private Button rejectRequest;

    private StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://orientationapp-328eb.appspot.com");

    public requestsAdapter(Context context, ArrayList<Person> persons) {
        super(context, R.layout.request_item,persons);
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
        view = inflater.inflate(R.layout.request_item,null);
        final Person person = persons.get(i);

        final MyApplication mApp = (MyApplication) context.getApplicationContext();

        TextView name = (TextView)view.findViewById(R.id.name);
        TextView school = (TextView)view.findViewById(R.id.school);
        TextView profileType = (TextView)view.findViewById(R.id.profileType);
        final ImageView profilePicture = (ImageView) view.findViewById(R.id.profilePicture);
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

        acceptRequest = (Button)view.findViewById(R.id.acceptRequest);
        acceptRequest.getBackground().setColorFilter(context.getResources().getColor(R.color.nustLight), PorterDuff.Mode.MULTIPLY);
        acceptRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //update connection lists
                Person self = mApp.getLoggedInPerson();
                self.setUnread(0);
                FirebaseDatabase.getInstance().getReference().child("users").child("connections").child(person.getUid()).child(mApp.getLoggedInId()).setValue(self);

                Person other = person;
                other.setUnread(0);

                FirebaseDatabase.getInstance().getReference().child("users").child("connections").child(mApp.getLoggedInId()).child(person.getUid()).setValue(other);

                //remove request
                FirebaseDatabase.getInstance().getReference().child("users").child("requests").child(mApp.getLoggedInId()).child(person.getUid()).removeValue();

                persons.remove(person);
                notifyDataSetChanged();
            }
        });

        rejectRequest = (Button)view.findViewById(R.id.rejectRequest);
        rejectRequest.getBackground().setColorFilter(context.getResources().getColor(R.color.nustLight), PorterDuff.Mode.MULTIPLY);
        rejectRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("users").child("requests").child(mApp.getLoggedInId()).child(person.getUid()).removeValue();
                persons.remove(person);
                notifyDataSetChanged();
            }
        });

        return view;
    }
}
