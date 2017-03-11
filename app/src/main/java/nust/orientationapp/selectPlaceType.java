package nust.orientationapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class selectPlaceType extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_place_type);

        ListView lv = (ListView) findViewById(R.id.placeTypes);
        ArrayAdapter<CharSequence> placeTypeAdapter = ArrayAdapter.createFromResource(this,R.array.placeTypes,android.R.layout.simple_list_item_1);
        lv.setAdapter(placeTypeAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.putExtra("position",i);
                setResult(22,intent);
                finish();
            }
        });
    }
}
