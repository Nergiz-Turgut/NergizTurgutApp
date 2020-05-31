package com.example.nergizturgutapp.presentation.controller;

import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.nergizturgutapp.Constants;
import com.example.nergizturgutapp.Singletons;
import com.example.nergizturgutapp.presentation.model.Pokemon;
import com.example.nergizturgutapp.presentation.model.RestPokemonResponse;
import com.example.nergizturgutapp.presentation.view.MainActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainController {

    SharedPreferences sharedPreferences;
    Gson gson;
    MainActivity view;

    public MainController(MainActivity mainActivity, Gson gson, SharedPreferences sharedPreferences) {
        this.view = mainActivity;
        this.gson = gson;
        this.sharedPreferences = sharedPreferences;
    }

    public void onStart(){
        List<Pokemon> pokemonList = getDataFromCache();

        if(pokemonList != null){
            view.showList(pokemonList);
        }else makeApiCall();
    }

    private List<Pokemon> getDataFromCache() {
        String jsonPokemon =  sharedPreferences.getString(Constants.KEY_POKEMON_LIST, null);
        if(jsonPokemon == null){
            return null;
        }else{
            Type listeType = new TypeToken<List<Pokemon>>(){}.getType();
            return gson.fromJson(jsonPokemon, listeType);
        }
    }

    private void makeApiCall() {
        Call<RestPokemonResponse> call = Singletons.getPokeApi().getPokemonResponse();
        call.enqueue(new Callback<RestPokemonResponse>() {

            @Override
            public void onResponse(Call<RestPokemonResponse> call, Response<RestPokemonResponse> response) {
                if(response.isSuccessful() && response.body() !=null){
                    List<Pokemon> pokemonList = response.body().getResults();
                    saveList(pokemonList);
                    view.showList(pokemonList);
                }else view.showError();
            }
            private void saveList(List<Pokemon> pokemonList) {
                String jsonString = gson.toJson(pokemonList);
                sharedPreferences
                        .edit()
                        .putString("jsonPokemonList", "jsonString")
                        .apply();
                Toast.makeText(view.getApplicationContext(), "List sauvegardée", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<RestPokemonResponse> call, Throwable t) {
                view.showError();
            }
        });
    }
    private void saveList(List<Pokemon> pokemonList) {
        String jsonString = gson.toJson(pokemonList);
        sharedPreferences
                .edit()
                .putString(Constants.KEY_POKEMON_LIST, jsonString)
                .apply();
    }
}

