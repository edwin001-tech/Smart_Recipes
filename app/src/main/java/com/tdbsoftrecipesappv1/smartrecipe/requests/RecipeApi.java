package com.tdbsoftrecipesappv1.smartrecipe.requests;



import androidx.lifecycle.LiveData;

import com.tdbsoftrecipesappv1.smartrecipe.requests.responses.ApiResponse;
import com.tdbsoftrecipesappv1.smartrecipe.requests.responses.RecipeSearchResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeApi {

    // SEARCH RECIPES
    @GET("search")
    LiveData<ApiResponse<RecipeSearchResponse>> searchRecipes(
            @Query("app_id") String appId,
            @Query("app_key") String appKey,
            @Query("q") String query,
            @Query("from") String from,
            @Query("to") String to
    );
}
