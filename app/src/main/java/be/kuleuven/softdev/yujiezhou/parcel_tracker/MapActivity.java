package be.kuleuven.softdev.yujiezhou.parcel_tracker;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

// Google Map activity with tracking details lined on map
public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    int length = DisplayActivity.responselength;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;


        map.moveCamera(CameraUpdateFactory.zoomOut());
        // Draw the lines between each place the package passes
        for (int i = 0; i < length - 1; i++) {
            map.addPolyline(new PolylineOptions().geodesic(true)
                    .add(new LatLng(DisplayActivity.locations.get(i).getLat(), DisplayActivity.locations.get(i).getLng()))
                    .add(new LatLng(DisplayActivity.locations.get(i + 1).getLat(), DisplayActivity.locations.get(i + 1).getLng()))
                    .color(0xFFFF9F00));
        }

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(DisplayActivity.locations.get(length - 1).getLat(), DisplayActivity.locations.get(length - 1).getLng()), 5));

    }
}
