package siarhei.luskanau.places.api.web.model;

import com.google.gson.annotations.SerializedName;

public class Viewport {

    @SerializedName("northeast")
    public LatLng northeast;

    @SerializedName("southwest")
    public LatLng southwest;

}