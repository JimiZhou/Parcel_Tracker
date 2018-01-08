package be.kuleuven.softdev.yujiezhou.parcel_tracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

// DisplayActivity of application, displays tracking information
public class DisplayActivity extends AppCompatActivity {

    private static final String TAG = "DisplayActivity";
    static String ORIGIN = "origin";
    static String DESTINATION = "destination";
    static String CURRENT_LOCATION = "current_location";
    static String DATE = "Date_time";
    static String DISCRIPTION = "description";
    static ArrayList<MyLocation> locations;
    static ArrayList<HashMap<String, Double>> arraylist_lat;
    static ArrayList<HashMap<String, Double>> arraylist_lng;
    static int responselength = 0;
    ListView listview;
    ListViewAdapter adapter;
    ArrayList<HashMap<String, String>> arraylist;
    ListView listview_detail;
    ListViewAdapter adapter_detail;
    ArrayList<HashMap<String, String>> arraylist_detail;
    String result = MainActivity.result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        String display = "Your Tracking number is: " + result + ".";
        TextView mResultTextView = (TextView) findViewById(R.id.textView8);
        mResultTextView.setText(display);

        // Initialize the arraylist to store the data of tracking info
        arraylist = new ArrayList<HashMap<String, String>>();

        locations = new ArrayList<>();
        // Locate the listview in listview_main.xml
        listview = (ListView) findViewById(R.id.listview_item);
        // Pass the results into ListViewAdapter.java
        adapter = new ListViewAdapter(DisplayActivity.this, arraylist);
        // Set the adapter to the ListView
        listview.setAdapter(adapter);
        listview.setEnabled(false);
        //Get the data from database
        trackinfo();


        arraylist_detail = new ArrayList<HashMap<String, String>>();
        //Locate the listview
        listview_detail = (ListView) findViewById(R.id.listview_detail);
        // Pass the results into ListViewAdapter.java
        adapter_detail = new ListViewAdapter(DisplayActivity.this, arraylist_detail);
        // Set the adapter to the ListView
        listview_detail.setAdapter(adapter_detail);
        listview_detail.setClickable(false);

    }


    public void trackinfo() {

        // Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(this);

        // Get input tracking number, generate the url
        final String general_url = "http://api.a17-sd210.studev.groept.be/SearchService/" + result;


        // Request a string response from the provided URL.
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, general_url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            responselength = response.length();
                            // Loop through the array elements
                            for (int i = response.length() - 1; i < response.length(); i++) {
                                HashMap<String, String> map = new HashMap<String, String>();
                                // Get current json object
                                JSONObject info = (JSONObject) response.get(i);
                                // Get the current tracking info (json object) data
                                map.put("origin", info.getString("origin").replace("%20", " "));
                                map.put("destination", info.getString("destination").replace("%20", " "));
                                map.put("current_location", info.getString("current_location").replace("%20", " "));
                                // Set the JSON Objects into the array
                                arraylist.add(map);
                            }

                            // Loop through the array elements
                            for (int j = 0; j < response.length(); j++) {
                                HashMap<String, String> map = new HashMap<String, String>();
                                // Get current json object
                                JSONObject info = (JSONObject) response.get(j);
                                // Get the current tracking info (json object) data
                                map.put("current_location", info.getString("current_location").replace("%20", " "));
                                map.put("Date_time", info.getString("Date_time").replace("%20", " "));
                                map.put("description", info.getString("description").replace("%20", " "));
                                // Set the JSON Objects into the array
                                arraylist_detail.add(map);
                                MyLocation location = new MyLocation(info.getDouble("Lat"), info.getDouble("Lng"));
                                locations.add(location);
                            }

                            // Refresh the listview when data is fetched
                            adapter.notifyDataSetChanged();
                            adapter_detail.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
            }
        });

        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest);


    }

    // Show the tracking on Google Map
    public void ShowMap(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

}

