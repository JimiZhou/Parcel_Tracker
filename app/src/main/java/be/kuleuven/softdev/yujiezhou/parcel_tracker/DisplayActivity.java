package be.kuleuven.softdev.yujiezhou.parcel_tracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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



public class DisplayActivity extends AppCompatActivity {

    static String ORIGIN = "origin";
    static String DESTINATION = "destination";
    static String CURRENT_LOCATION = "current_location";
    ListView listview;
    ListViewAdapter adapter;
    ArrayList<HashMap<String, String>> arraylist;
    private static final String TAG = "DisplayActivity";
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

        trackinfo();
    }

    public void trackinfo(){


        // Create an Array
        arraylist = new ArrayList<HashMap<String, String>>();

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Get input tracking number, generate the url
        String url ="http://studev.groept.be/api/a17_sd210/SearchService/"+result;

        // Request a string response from the provided URL.
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response) {

                        try{
                            // Loop through the array elements
                            for(int i=0;i<response.length();i++) {
                                HashMap<String, String> map = new HashMap<String, String>();
                                // Get current json object
                                JSONObject info = (JSONObject) response.get(i);
                                // Get the current tracking info (json object) data
                                map.put("origin", info.getString("origin"));
                                map.put("destination", info.getString("destination"));
                                map.put("current_location", info.getString("current_location"));
                                // Set the JSON Objects into the array
                                arraylist.add(map);
                            }


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

        // Locate the listview in listview_main.xml
        listview = (ListView) findViewById(R.id.listview_item);
        // Pass the results into ListViewAdapter.java
        adapter = new ListViewAdapter(DisplayActivity.this, arraylist);
        // Set the adapter to the ListView
        listview.setAdapter(adapter);
    }





    public void ShowMap(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

}

