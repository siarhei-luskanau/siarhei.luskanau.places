package siarhei.luskanau.places.data.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlacesResponse extends BaseResponse {

    @SerializedName("results")
    private List<PlaceEntity> results;

    public List<PlaceEntity> getResults() {
        return results;
    }

    public void setResults(List<PlaceEntity> results) {
        this.results = results;
    }

}