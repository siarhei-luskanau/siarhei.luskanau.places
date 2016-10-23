package siarhei.luskanau.places.api.web.model;

import com.google.gson.annotations.SerializedName;

public class Geometry {

    @SerializedName("location")
    public LatLng location;

    @SerializedName("viewport")
    public Viewport viewport;

}