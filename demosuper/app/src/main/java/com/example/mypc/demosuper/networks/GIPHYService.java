package com.example.mypc.demosuper.networks;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GIPHYService {
    @GET("search")
    Call<GifResponse> getGifResponse (@Query("q") String key, @Query("limit") Integer limit, @Query("api_key") String apiKey);

}
