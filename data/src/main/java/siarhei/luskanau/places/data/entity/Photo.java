package siarhei.luskanau.places.data.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Photo {

    @SerializedName("photo_reference")
    private String photoReference;
    @SerializedName("width")
    private int width;
    @SerializedName("height")
    private int height;
    @SerializedName("html_attributions")
    private List<Object> htmlAttributions;
    private String photoUrl;

    public String getPhotoReference() {
        return photoReference;
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public List<Object> getHtmlAttributions() {
        return htmlAttributions;
    }

    public void setHtmlAttributions(List<Object> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}