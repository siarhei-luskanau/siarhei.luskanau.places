package siarhei.luskanau.places.api.web.model;

import com.google.gson.annotations.SerializedName;

public class PlaceDetailsResponse extends BaseResponse {

    @SerializedName("result")
    private Place result;

    public Place getResult() {
        return result;
    }

    public void setResult(Place result) {
        this.result = result;
    }

}