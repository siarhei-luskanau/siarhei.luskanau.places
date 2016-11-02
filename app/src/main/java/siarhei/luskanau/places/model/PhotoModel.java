package siarhei.luskanau.places.model;

import siarhei.luskanau.places.utils.glide.PlacePhotoId;

public class PhotoModel {

    private String photoUrl;

    private PlacePhotoId placePhotoId;

    public PhotoModel(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public PhotoModel(PlacePhotoId placePhotoId) {
        this.placePhotoId = placePhotoId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public PlacePhotoId getPlacePhotoId() {
        return placePhotoId;
    }

    public void setPlacePhotoId(PlacePhotoId placePhotoId) {
        this.placePhotoId = placePhotoId;
    }

}