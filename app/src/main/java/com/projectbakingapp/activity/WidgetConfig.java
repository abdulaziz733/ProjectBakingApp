package com.projectbakingapp.activity;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.projectbakingapp.R;
import com.projectbakingapp.adapter.ListRecipeAdapter;
import com.projectbakingapp.api.ApiInterface;
import com.projectbakingapp.model.Ingredient;
import com.projectbakingapp.model.Recipe;
import com.projectbakingapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class WidgetConfig extends AppCompatActivity implements ListRecipeAdapter.ListRecipeAdapterListener {

    private static final String ADAPTER_TYPE_CONFIG = "ADAPTER_TYPE_CONFIG";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.list_recipe)
    RecyclerView recyclerView;

    private ApiInterface apiInterface;
    private List<Recipe> recipeList;
    private ListRecipeAdapter recipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_config);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        apiInterface = Utils.getAPIService();
        recipeList = new ArrayList<Recipe>();
        recipeAdapter = new ListRecipeAdapter(recipeList, WidgetConfig.this, WidgetConfig.this, ADAPTER_TYPE_CONFIG);
        recyclerView.setLayoutManager(new LinearLayoutManager(WidgetConfig.this));
        recyclerView.setAdapter(recipeAdapter);

        getData();
    }


    private void getData(){
        try {
            apiInterface.getListRecipe().enqueue(new Callback<List<Recipe>>() {
                @Override
                public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                    if (response.isSuccessful()) {
                        recipeList.clear();
                        for (Recipe recipe : response.body()) {
                            recipeList.add(recipe);
                        }
                        recipeAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<List<Recipe>> call, Throwable t) {
                    Timber.d("error", t.getMessage());
                }
            });

        } catch (Exception e) {
            Timber.d("error", e.getMessage());
        }
    }

    @Override
    public void onStepClicked(int position) {
        Gson gson = new Gson();
        List<Ingredient> ingredients = recipeList.get(position).getIngredients();
        String recipeName = recipeList.get(position).getName();
        Utils.saveDataIngredient(WidgetConfig.this, (ArrayList<Ingredient>) ingredients, recipeName);

        int mAppWidgetId = 0;

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(WidgetConfig.this);

        RemoteViews views = new RemoteViews(WidgetConfig.this.getPackageName(),
                R.layout.ingredients_widget_provider);
        appWidgetManager.updateAppWidget(mAppWidgetId, views);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    @Override
    public void onIngredientsClicked(int position) {

    }
}
