package com.example.nergizturgutapp;

import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ListAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private static final String BASE_URL = "https://api.printful.com";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        makeApiCall();


    }

    private void showList(List<Country>countryList) {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new ListAdapter(countryList);
        recyclerView.setAdapter(mAdapter);

    }



    private void makeApiCall() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        CountryAPI countryApi = retrofit.create(CountryAPI.class);

        Call<RestCountryResponse> call = countryApi.getCountryResponse();
        call.enqueue(new Callback<RestCountryResponse>() {
                @Override
            public void onResponse(Call<RestCountryResponse> call, Response<RestCountryResponse> response) {
                if(response.isSuccessful() && response.body() !=null){
                    List<Country> countryList = response.body().getResults();
                    showList(countryList);
                }else showError();

            }

            @Override
            public void onFailure(Call<RestCountryResponse> call, Throwable t) {
                showError();
            }
        });

    }

    private void showError() {
        Toast.makeText(getApplicationContext(), "API Error", Toast.LENGTH_SHORT).show();
    }


}
