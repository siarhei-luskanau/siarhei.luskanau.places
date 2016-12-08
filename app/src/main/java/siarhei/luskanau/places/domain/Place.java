package siarhei.luskanau.places.domain;

import java.util.List;

public class Place {

    private final String id;
    private CharSequence name;
    private CharSequence address;
    private CharSequence phoneNumber;
    private String websiteUri;
    private double latitude;
    private double longitude;
    private List<Photo> photos;

    public Place(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public CharSequence getName() {
        return name;
    }

    public void setName(CharSequence name) {
        this.name = name;
    }

    public CharSequence getAddress() {
        return address;
    }

    public void setAddress(CharSequence address) {
        this.address = address;
    }

    public CharSequence getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(CharSequence phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWebsiteUri() {
        return websiteUri;
    }

    public void setWebsiteUri(String websiteUri) {
        this.websiteUri = websiteUri;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    @Override
    public String toString() {
        return new StringBuilder(80)
                .append("Place Details: id=").append(this.getId())
                .append(" name=").append(this.getName())
                .append(" address=").append(this.getAddress())
                .toString();
    }
}