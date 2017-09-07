package com.projectbakingapp.api;

import com.projectbakingapp.model.Recipe;
import com.projectbakingapp.utils.Constant;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * Created by abdul on 6/13/2017.
 */

public interface ApiInterface {

    @GET(Constant.DATA_URL)
    Call<List<Recipe>> getListRecipe();

}
