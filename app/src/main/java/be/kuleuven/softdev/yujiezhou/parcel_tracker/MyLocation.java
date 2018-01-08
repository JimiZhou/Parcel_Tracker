package be.kuleuven.softdev.yujiezhou.parcel_tracker;

/**
 * Created by yujiezhou on 19/12/2017.
 */

public class MyLocation {

    private Double lat;
    private Double lng;

    public MyLocation(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public Double getLat() {

        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}