/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package siarhei.luskanau.places.data.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Place Entity used in the data layer.
 */
public class PlaceEntity {

    @SerializedName("address_components")
    private Object addressComponents;
    @SerializedName(value = "formattedAddress", alternate = "formatted_address")
    private String formattedAddress;
    @SerializedName("formatted_phone_number")
    private String formattedPhoneNumber;
    @SerializedName("geometry")
    private Geometry geometry;
    @SerializedName("icon")
    private Object icon;
    @SerializedName("international_phone_number")
    private String internationalPhoneNumber;
    @SerializedName("name")
    private String name;
    @SerializedName("opening_hours")
    private Object openingHours;
    @SerializedName("photos")
    private List<Photo> photos;
    @SerializedName("place_id")
    private String placeId;
    @SerializedName("scope")
    private Object scope;
    @SerializedName("price_level")
    private Object priceLevel;
    @SerializedName("rating")
    private Object rating;
    @SerializedName("reference")
    private Object reference;
    @SerializedName("reviews")
    private List<Object> reviews;
    @SerializedName("types")
    private List<Object> types;
    @SerializedName("url")
    private String url;
    @SerializedName("utc_offset")
    private long utcOffset;
    @SerializedName("vicinity")
    private String vicinity;
    @SerializedName("website")
    private String website;

    public PlaceEntity() {
        //empty
    }

    public Object getAddressComponents() {
        return addressComponents;
    }

    public void setAddressComponents(Object addressComponents) {
        this.addressComponents = addressComponents;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public String getFormattedPhoneNumber() {
        return formattedPhoneNumber;
    }

    public void setFormattedPhoneNumber(String formattedPhoneNumber) {
        this.formattedPhoneNumber = formattedPhoneNumber;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public Object getIcon() {
        return icon;
    }

    public void setIcon(Object icon) {
        this.icon = icon;
    }

    public String getInternationalPhoneNumber() {
        return internationalPhoneNumber;
    }

    public void setInternationalPhoneNumber(String internationalPhoneNumber) {
        this.internationalPhoneNumber = internationalPhoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(Object openingHours) {
        this.openingHours = openingHours;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public Object getScope() {
        return scope;
    }

    public void setScope(Object scope) {
        this.scope = scope;
    }

    public Object getPriceLevel() {
        return priceLevel;
    }

    public void setPriceLevel(Object priceLevel) {
        this.priceLevel = priceLevel;
    }

    public Object getRating() {
        return rating;
    }

    public void setRating(Object rating) {
        this.rating = rating;
    }

    public Object getReference() {
        return reference;
    }

    public void setReference(Object reference) {
        this.reference = reference;
    }

    public List<Object> getReviews() {
        return reviews;
    }

    public void setReviews(List<Object> reviews) {
        this.reviews = reviews;
    }

    public List<Object> getTypes() {
        return types;
    }

    public void setTypes(List<Object> types) {
        this.types = types;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getUtcOffset() {
        return utcOffset;
    }

    public void setUtcOffset(long utcOffset) {
        this.utcOffset = utcOffset;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

}