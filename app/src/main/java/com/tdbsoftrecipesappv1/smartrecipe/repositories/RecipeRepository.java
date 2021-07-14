package com.tdbsoftrecipesappv1.smartrecipe.repositories;


import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.tdbsoftrecipesappv1.smartrecipe.AppExecutors;
import com.tdbsoftrecipesappv1.smartrecipe.models.Recipe;
import com.tdbsoftrecipesappv1.smartrecipe.persistence.RecipeDao;
import com.tdbsoftrecipesappv1.smartrecipe.persistence.RecipeDatabase;
import com.tdbsoftrecipesappv1.smartrecipe.requests.ServiceGenerator;
import com.tdbsoftrecipesappv1.smartrecipe.requests.responses.ApiResponse;
import com.tdbsoftrecipesappv1.smartrecipe.requests.responses.RecipeSearchResponse;
import com.tdbsoftrecipesappv1.smartrecipe.util.Constants;
import com.tdbsoftrecipesappv1.smartrecipe.util.NetworkBoundResource;
import com.tdbsoftrecipesappv1.smartrecipe.util.Resource;

import java.util.ArrayList;
import java.util.List;

public class RecipeRepository {

    private static final String TAG = "RecipeRepository";

    private static RecipeRepository instance;
    private RecipeDao recipeDao;

    public static RecipeRepository getInstance(Context context) {
        if (instance == null) {
            instance = new RecipeRepository(context);
        }
        return instance;
    }

    public RecipeRepository(Context context) {
        recipeDao = RecipeDatabase.getInstance(context).getRecipeDao();
    }

    public LiveData<Resource<List<Recipe>>> searchRecipesApi(final String query, final int pageNumber, final int pageRecipeFrom, final int pageRecipeTo) {
        return new NetworkBoundResource<List<Recipe>, RecipeSearchResponse>(AppExecutors.getInstance()) {
            @Override
            protected void saveCallResult(@NonNull RecipeSearchResponse item) {
                if (item.getRecipes() != null) {
                    Recipe[] recipes = new Recipe[item.getRecipes().size()];
                    List<Recipe> mRecipes = new ArrayList<>();
                    for (int i = 0; i < item.getRecipes().size(); i++) {
                        mRecipes.add(item.getRecipes().get(i).getRecipe());
                        mRecipes.get(i).setQueryFlag(query);
                    }

                    int index = 0;
                    for (long rowId : recipeDao.insertRecipes((Recipe[])(mRecipes.toArray(recipes)))) {
                        if (rowId == -1) {
                            Log.d(TAG, "saveCallResult: CONFLICT... this recipe is already in the cache");
                            recipeDao.updateRecipe(
                                    recipes[index].getUri(),
                                    recipes[index].getLabel(),
                                    recipes[index].getImage(),
                                    recipes[index].getSource(),
                                    recipes[index].getCalories(),
                                    recipes[index].getQueryFlag()
                            );
                        }
                        index++;
                    }
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Recipe> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Recipe>> loadFromDb() {
                return recipeDao.searchRecipes(query, pageNumber);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<RecipeSearchResponse>> createCall() {
                return ServiceGenerator.getRecipeApi().searchRecipes(Constants.APP_ID, Constants.APP_KEY, query, String.valueOf(pageRecipeFrom), String.valueOf(pageRecipeTo));
            }
        }.getAsLiveData();
    }
}
