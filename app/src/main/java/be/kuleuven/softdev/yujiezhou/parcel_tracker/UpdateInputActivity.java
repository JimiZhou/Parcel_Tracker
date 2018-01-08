package be.kuleuven.softdev.yujiezhou.parcel_tracker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;

import org.json.JSONArray;

import java.util.Calendar;

// UpdateInput gets the information needed for a package
public class UpdateInputActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "UpdateInputActivity";
    private GoogleApiClient mGoogleApiClient;
    private int PLACE_PICKER_REQUEST = 1;
    private FloatingActionButton fabPickPlace;
    private TextView tvPlaceDetails;
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private String Datetime;
    private int year, month, day;
    private String placename = "";
    private String latitude = "";
    private String longitude = "";
    private String address = "";
    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2 + 1, arg3);
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_input);

        //Initialize the toolbar and menu
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        tvPlaceDetails = (TextView) findViewById(R.id.placedetails);
        fabPickPlace = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        TextView textView = (TextView) findViewById(R.id.textView12);
        textView.setText(UpdateActivity.result_update);

        //Initialize the dateView
        dateView = (TextView) findViewById(R.id.Date);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month + 1, day);

        //Google Place Picker API
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        fabPickPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(UpdateInputActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {

        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private void showDate(int year, int month, int day) {
        Datetime = new StringBuilder().append(day).append(".")
                .append(month).append(".").append(year).toString();
        dateView.setText(Datetime);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Snackbar.make(fabPickPlace, connectionResult.getErrorMessage() + "", Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                StringBuilder stBuilder = new StringBuilder();
                placename = String.format("%s", place.getName());
                latitude = String.valueOf(place.getLatLng().latitude);
                longitude = String.valueOf(place.getLatLng().longitude);
                address = String.format("%s", place.getAddress());
                stBuilder.append("Name: ");
                stBuilder.append(placename);
                stBuilder.append("\n");
                stBuilder.append("Latitude: ");
                stBuilder.append(latitude);
                stBuilder.append("\n");
                stBuilder.append("Logitude: ");
                stBuilder.append(longitude);
                stBuilder.append("\n");
                stBuilder.append("Address: ");
                stBuilder.append(address);
                tvPlaceDetails.setText(stBuilder.toString());
            }
        }
    }

    public void UpdateTrackInfo(final View view) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Get input email and password, generate the url
        final TextView origin = (TextView) findViewById(R.id.editText_origin);
        final TextView destination = (TextView) findViewById(R.id.editText_destination);
        final TextView description = (TextView) findViewById(R.id.editText_description);
        String Origin = origin.getText().toString().replaceAll(" ", "%20");
        String Destination = destination.getText().toString().replaceAll(" ", "%20");
        String Descripiton = description.getText().toString().replaceAll(" ", "%20");

        String url = "http://api.a17-sd210.studev.groept.be/UpdateService/" + Origin + "/" + Destination + "/" + placename + "/" + UpdateActivity.result_update + "/" + Datetime + "/" + Descripiton + "/" + latitude + "/" + longitude;

        if (Origin.isEmpty()) {
            // / Empty origin! Show a toast to indicate empty origin
            Snackbar.make(findViewById(R.id.MainCoordinatorLayout), "Please input Origin!", Snackbar.LENGTH_SHORT).show();
        } else if (Destination.isEmpty()) {

            // / Empty destination! Show a toast to indicate empty destination
            Snackbar.make(findViewById(R.id.MainCoordinatorLayout), "Please input Destination!", Snackbar.LENGTH_SHORT).show();
        } else if (Descripiton.isEmpty()) {

            // / Empty description! Show a toast to indicate empty description
            Snackbar.make(findViewById(R.id.MainCoordinatorLayout), "Please input Description!", Snackbar.LENGTH_SHORT).show();
        } else if (placename.isEmpty()) {
            // / No location! Show a toast to indicate empty location
            Snackbar.make(findViewById(R.id.MainCoordinatorLayout), "Please select a location!", Snackbar.LENGTH_SHORT).show();
        } else {
            // Request a string response from the provided URL.
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {

                            // / Update successful! Show a toast to indicate successful update
                            Snackbar.make(findViewById(R.id.MainCoordinatorLayout), "Update Successful", Snackbar.LENGTH_SHORT).show();


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
}
