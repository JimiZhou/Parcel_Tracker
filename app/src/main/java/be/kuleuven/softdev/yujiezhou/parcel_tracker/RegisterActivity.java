package be.kuleuven.softdev.yujiezhou.parcel_tracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
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

// Register function, provides Email unique check and validation, empty check and password length check
public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void RegisterCheck(View view) {
        // Get input email, generate the url
        final TextView InputName = (TextView) findViewById(R.id.editText1);
        final TextView InputEmail = (TextView) findViewById(R.id.editText2);
        final TextView InputPassword = (TextView) findViewById(R.id.editText3);
        String Name = InputName.getText().toString();
        String Email = InputEmail.getText().toString();
        String Password = InputPassword.getText().toString();
        String check = "http://api.a17-sd210.studev.groept.be/CheckService/" + Email;

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        if (Name.isEmpty()) {
            Snackbar.make(findViewById(R.id.RegisterCoordinatorLayout), "Please input your name!", Snackbar.LENGTH_SHORT).show();
        } else if(!isValidEmail(Email)) {
            Snackbar.make(findViewById(R.id.RegisterCoordinatorLayout), "Please input correct E-mail address!", Snackbar.LENGTH_SHORT).show();
        } else if(Email.isEmpty()){
            Snackbar.make(findViewById(R.id.RegisterCoordinatorLayout), "Please input your E-mail!", Snackbar.LENGTH_SHORT).show();
        } else if (Password.isEmpty()) {
            Snackbar.make(findViewById(R.id.RegisterCoordinatorLayout), "Please input your password!", Snackbar.LENGTH_SHORT).show();
        } else if (Password.length()<4) {
            Snackbar.make(findViewById(R.id.RegisterCoordinatorLayout), "Password minimum length is 4!", Snackbar.LENGTH_SHORT).show();
        } else {
            // Request a string response from the provided URL.
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, check, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {

                            if (response.length() == 0) {
                                // Show a toast to indicate successful register
                                final Snackbar snackbar = Snackbar.make(findViewById(R.id.RegisterCoordinatorLayout), "Register success!", Snackbar.LENGTH_INDEFINITE);
                                // Set an action on it, and a handler
                                snackbar.setAction("Login", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        snackbar.dismiss();
                                        RegisterWrite();
                                    }
                                });
                                snackbar.show();
                            } else {
                                // / Email alreday exits! Show a toast to indicate unsuccessful register
                                Snackbar.make(findViewById(R.id.RegisterCoordinatorLayout), "E-mail already registered!", Snackbar.LENGTH_SHORT).show();
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

    public void RegisterWrite() {
        // Get input email, generate the url
        final TextView InputName = (TextView) findViewById(R.id.editText1);
        final TextView InputEmail = (TextView) findViewById(R.id.editText2);
        final TextView InputPassword = (TextView) findViewById(R.id.editText3);
        String Name = InputName.getText().toString();
        String Email = InputEmail.getText().toString();
        String Password = InputPassword.getText().toString();
        String register ="http://studev.groept.be/api/a17_sd210/RegisterService/"+Name+"/"+Email+"/"+Password;
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        // Request a string response from the provided URL.
        JsonArrayRequest jsonArrayRequest1 = new JsonArrayRequest(Request.Method.GET, register,null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
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
        queue.add(jsonArrayRequest1);
        // Register success, go back to login
        RegisterSuccess();
    }


    public void RegisterSuccess() {
        // Success! Go back to LoginActivity
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
