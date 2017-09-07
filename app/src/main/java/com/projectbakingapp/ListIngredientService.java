package com.projectbakingapp;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.Toast;

import com.projectbakingapp.api.ApiInterface;
import com.projectbakingapp.data.TempData;
import com.projectbakingapp.model.Ingredient;
import com.projectbakingapp.model.Recipe;
import com.projectbakingapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by abdul on 9/1/2017.
 */

public class ListIngredientService extends RemoteViewsService {
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    Context context;
    List<Ingredient> ingredientList = new ArrayList<Ingredient>();
    ApiInterface apiInterface;

    public ListRemoteViewsFactory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        try {
            ingredientList.clear();
            ingredientList = Utils.getSavedIngredient(context);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onDataSetChanged() {
        try {
            ingredientList.clear();
            ingredientList = Utils.getSavedIngredient(context);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return ingredientList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Ingredient ingredient = ingredientList.get(position);
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredient_item_widget);
        views.setTextViewText(R.id.ingredient_name_item, ingredient.getIngredient());
        views.setTextViewText(R.id.quantity_item, String.valueOf(ingredient.getQuantity()));
        views.setTextViewText(R.id.measure_item, ingredient.getMeasure());

        Intent fillInIntent = new Intent();

        views.setOnClickFillInIntent(R.id.ing_widget_view, fillInIntent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
