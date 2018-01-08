package be.kuleuven.softdev.yujiezhou.parcel_tracker;

/**
 * Created by yujiezhou on 05/12/2017.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SingleItemView extends Activity {
    // Declare Variables
    String origin;
    String destination;
    String current_location;
    String date_time;
    String description;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from singleitemview.xml
        setContentView(R.layout.singleitemview);

        Intent i = getIntent();
        // Get the result of rank
        origin = i.getStringExtra("origin");
        // Get the result of country
        destination = i.getStringExtra("destination");
        // Get the result of population
        current_location = i.getStringExtra("current_location");

        date_time = i.getStringExtra("Date_time");

        description = i.getStringExtra("description");


        // Locate the TextViews in singleitemview.xml
        TextView txtorigin = (TextView) findViewById(R.id.origin);
        TextView txtdestination = (TextView) findViewById(R.id.destination);
        TextView txtcurrent_location = (TextView) findViewById(R.id.current_location);

        // Set results to the TextViews
        txtorigin.setText(origin);
        txtdestination.setText(destination);
        txtcurrent_location.setText(current_location);

    }
}