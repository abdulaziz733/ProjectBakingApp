package com.projectbakingapp.data;

import com.projectbakingapp.model.Ingredient;

import java.util.ArrayList;

/**
 * Created by abdul on 9/3/2017.
 */

public class TempData {

    public static ArrayList<Ingredient> ingredientArrayList = new ArrayList<Ingredient>();

    public ArrayList<Ingredient> getIngredientArrayList() {
        return ingredientArrayList;
    }

    public void setIngredientArrayList(ArrayList<Ingredient> ingredientArrayList) {
        this.ingredientArrayList = ingredientArrayList;
    }
}
