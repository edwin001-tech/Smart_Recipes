package com.tdbsoftrecipesappv1.smartrecipe.requests.responses;

import com.tdbsoftrecipesappv1.smartrecipe.models.Recipes;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecipeSearchResponse {

    @SerializedName("hits")
    @Expose()
    private List<Recipes> recipes;

    public List<Recipes> getRecipes() {
        return recipes;
    }

    @Override
    public String toString() {
        return "RecipeSearchResponse{" +
                "recipes=" + recipes +
                '}';
    }
}
