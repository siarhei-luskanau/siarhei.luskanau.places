package siarhei.luskanau.places.api.web.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BaseResponse<T> {

    public static final String OK = "OK";
    public static final String ZERO_RESULTS = "ZERO_RESULTS";
    public static final String OVER_QUERY_LIMIT = "OVER_QUERY_LIMIT";
    public static final String REQUEST_DENIED = "REQUEST_DENIED";
    public static final String INVALID_REQUEST = "INVALID_REQUEST";

    @SerializedName("html_attributions")
    public List<Object> htmlAttributions;

    @SerializedName("results")
    public T results;

    @SerializedName("status")
    public String status;

    @SerializedName("error_message")
    public String errorMessage;

}