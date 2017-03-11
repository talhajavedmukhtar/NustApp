package nust.orientationapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Talha on 8/31/16.
 */
public class searchResultsAdapter extends ArrayAdapter<Person> {
    ArrayList<Person> results;
    Context context;
    LayoutInflater inflater;

    private Button sendRequest;
    private Button viewProfile;

    StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://orientationapp-328eb.appspot.com");

    public searchResultsAdapter(ArrayList<Person> results, Context context){
        super(context,R.layout.searchresult_item,results);
        this.results = results;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return results.size();
    }

    @Override
    public Person getItem(int i) {
        return results.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.searchresult_item,null);
        final Person person = results.get(i);

        final MyApplication mApp = (MyApplication) context.getApplicationContext();

        TextView name = (TextView)view.findViewById(R.id.name);
        TextView school = (TextView)view.findViewById(R.id.school);
        TextView profileType = (TextView)view.findViewById(R.id.profileType);
        final ImageView profilePicture = (ImageView)view.findViewById(R.id.profilePicture);

        storageRef.child("ProfilePics").child(person.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(context).load(uri).transform(new CircleTransform()).fit().centerInside().error(R.drawable.placeholder).placeholder(R.drawable.progress_animation).into(profilePicture);
            }
        });

        name.setText(person.getFirstName()+" "+person.getLastName());
        school.setText(person.getSchool());
        profileType.setText(person.getProfileType());

        sendRequest = (Button)view.findViewById(R.id.sendRequest);
        sendRequest.getBackground().setColorFilter(context.getResources().getColor(R.color.nustLight), PorterDuff.Mode.MULTIPLY);
        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("users").child("requests").child(person.getUid()).child(mApp.getLoggedInId()).setValue(mApp.getLoggedInPerson());
                results.remove(person);
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


}
