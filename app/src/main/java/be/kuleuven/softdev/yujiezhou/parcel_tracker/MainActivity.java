package be.kuleuven.softdev.yujiezhou.parcel_tracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void GetInfo(View view) {  final TextView mTextView = (TextView) findViewById(R.id.text);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://studev.groept.be/api/a17_sd210/Test";

        // Request a string response from the provided URL.

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONArray>()
                {
                    @Override
            public void onResponse(JSONArray response) {

                        try{
                            // Loop through the array elements
                            for(int i=0;i<response.length();i++) {
                                // Get current json object
                                JSONObject person = (JSONObject) response.get(i);
                                // Get the current student (json object) data
                                String name = person.getString("name");
                                String email = person.getString("email");
                                String id = person.getString("id");
                                String password = person.getString("password");

                                // Display the formatted json data in text view
                                String result;
                                result = "ID: "+id + "\r\n" +"Name: "+name +"\r\n"+"Email: " + email+"\r\n"+"Password: "+password+"\r\n";
                                mTextView.append(result);
                                mTextView.append("*****************************"+"\r\n");

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

    }
    }




