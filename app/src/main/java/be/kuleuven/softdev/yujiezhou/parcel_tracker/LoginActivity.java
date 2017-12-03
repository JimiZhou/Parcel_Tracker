package be.kuleuven.softdev.yujiezhou.parcel_tracker;

import android.content.Context;
import android.content.Intent;
import android.renderscript.ScriptGroup;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    public static String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void LoginVerify(final View view) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Get input email and password, generate the url
        final TextView InputEmail = (TextView) findViewById(R.id.editText);
        final TextView InputPassword = (TextView) findViewById(R.id.editText2);
        String email = InputEmail.getText().toString();
        String password = InputPassword.getText().toString();
        String url ="http://studev.groept.be/api/a17_sd210/LoginService/"+email+"/"+password;

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
                                String email = person.getString("email");
                                String password = person.getString("password");
                                name = person.getString(("name"));
                                if(email!=null && password!=null)
                                {
                                    // Call the LoginSuccess to get into MainActivity.
                                    LoginSuccess();
                                }
                            }
                            if(response.length() == 0)
                            {
                                // / Login failed! Show a toast to indicate unsuccessful login
                                Snackbar.make(findViewById(R.id.LoginCoordinatorLayout),"Login Failed", Snackbar.LENGTH_SHORT).show();
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

    public void LoginSuccess()
    {
        // Success! Call the MainActivity.
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void RegisterActivity(View view) {
        // If user doesn't have an account, click to create a new one.
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
