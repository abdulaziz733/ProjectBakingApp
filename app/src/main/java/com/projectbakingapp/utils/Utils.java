package com.projectbakingapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.projectbakingapp.api.ApiClient;
import com.projectbakingapp.api.ApiInterface;
import com.projectbakingapp.model.Ingredient;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by abdul on 6/13/2017.
 */

public class Utils {

    private static Gson gson;
    private static SharedPreferencesUtils sharedPreferencesUserData;


    public static ApiInterface getAPIService() {
        return ApiClient.getClient(Constant.BASE_URL).create(ApiInterface.class);
    }

    public static void showToast(Context ctx, String message, int duration){
        Toast.makeText(ctx, message, duration).show();
    }

    public static void startThisActivity(Activity ctx, Class classTujuan){
        Intent intent = new Intent(ctx, classTujuan);
        ctx.startActivity(intent);
    }

    public static void startThisActivityWithParams(Activity ctx, Class classTujuan, String data, String key){
        Intent intent = new Intent(ctx, classTujuan);
        intent.putExtra(key, data);
        ctx.startActivity(intent);
    }

    public static void setFrameLayout(FragmentManager fragmentManager, int resId, Fragment targetFragment){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(resId, targetFragment);
        fragmentTransaction.commit();
    }

    public static void saveDataIngredient(Context context, ArrayList<Ingredient> data, String recipeName) {
        gson = new Gson();
        sharedPreferencesUserData = new SharedPreferencesUtils(context, "widgetData");
        String ingredientListchoosen = gson.toJson(data);
        sharedPreferencesUserData.storeData("widget_ingredient", ingredientListchoosen);
        sharedPreferencesUserData.storeData("widget_recipe_name", recipeName);
    }

    public static ArrayList<Ingredient> getSavedIngredient(Context context) throws Exception {
        gson = new Gson();
        sharedPreferencesUserData = new SharedPreferencesUtils(context, "widgetData");
        Type listType = new TypeToken<List<Ingredient>>(){}.getType();
        String widgetData = sharedPreferencesUserData.getPreferenceData("widget_ingredient");

        return gson.fromJson(widgetData, listType);
    }

    public static String getSavedIngredientRecipeName(Context context) throws Exception {
        gson = new Gson();
        sharedPreferencesUserData = new SharedPreferencesUtils(context, "widgetData");
        String recipeNameIngData = sharedPreferencesUserData.getPreferenceData("widget_recipe_name");

        return recipeNameIngData;
    }




}
