package siarhei.luskanau.places.data.entity;

import com.google.gson.annotations.SerializedName;

public class Geometry {

    @SerializedName("location")
    private LatLng location;

    @SerializedName("viewport")
    private Viewport viewport;

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }

}