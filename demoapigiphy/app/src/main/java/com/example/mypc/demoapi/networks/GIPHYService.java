package com.example.mypc.demoapi.networks;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GIPHYService {
    @GET("search")
    Call<GifResponse>  getGifResponse (@Query("q") String key, @Query("api_key") String apiKey);

}