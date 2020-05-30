package com.example.nergizturgutapp;

import java.util.List;

public class RestCountryResponse {
    private Integer count;
    private List<Country> result;

    public Integer getCount() {
        return count;
    }

    public List<Country> getResults() {
        return result;
    }
}
