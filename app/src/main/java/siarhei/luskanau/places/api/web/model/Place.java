package siarhei.luskanau.places.api.web.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Place {

    @SerializedName("icon")
    public Object icon;
    @SerializedName("id")
    public String id;
    @SerializedName("place_id")
    public String placeId;
    @SerializedName("geometry")
    public Geometry geometry;
    @SerializedName("name")
    public String name;
    @SerializedName("photos")
    public List<Object> photos;
    @SerializedName("scope")
    public Object scope;
    @SerializedName("price_level")
    public Object priceLevel;
    @SerializedName("rating")
    public Object rating;
    @SerializedName("reference")
    public Object reference;
    @SerializedName("types")
    public List<Object> types;
    @SerializedName("vicinity")
    public String vicinity;
    @SerializedName("formattedAddress")
    public String formattedAddress;

}