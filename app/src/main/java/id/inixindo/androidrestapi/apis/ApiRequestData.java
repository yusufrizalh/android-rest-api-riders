package id.inixindo.androidrestapi.apis;

import id.inixindo.androidrestapi.models.ModelResponse;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiRequestData {
    @GET("view.php")
    Call<ModelResponse> getAllData();
}
