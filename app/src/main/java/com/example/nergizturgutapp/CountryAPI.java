package com.example.nergizturgutapp;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CountryAPI {

    @GET("/countries")
    Call<RestCountryResponse> getCountryResponse ();


}
