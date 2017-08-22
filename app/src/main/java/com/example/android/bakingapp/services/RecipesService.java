package com.example.android.bakingapp.services;

import com.example.android.bakingapp.entities.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by vincent on 8/22/17.
 */

public interface RecipesService {
    /*
    * Get available recipes
    * */
    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> getRecipes();
}
