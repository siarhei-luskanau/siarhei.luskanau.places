package siarhei.luskanau.places.data.entity;

import com.google.gson.annotations.SerializedName;

public class PlaceDetailsResponse extends BaseResponse {

    @SerializedName("result")
    private PlaceEntity result;

    public PlaceEntity getResult() {
        return result;
    }

    public void setResult(PlaceEntity result) {
        this.result = result;
    }

}