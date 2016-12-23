package siarhei.luskanau.places.data.entity;

import com.google.gson.annotations.SerializedName;

public class Viewport {

    @SerializedName("northeast")
    private LatLng northeast;

    @SerializedName("southwest")
    private LatLng southwest;

    public LatLng getNortheast() {
        return northeast;
    }

    public void setNortheast(LatLng northeast) {
        this.northeast = northeast;
    }

    public LatLng getSouthwest() {
        return southwest;
    }

    public void setSouthwest(LatLng southwest) {
        this.southwest = southwest;
    }

}