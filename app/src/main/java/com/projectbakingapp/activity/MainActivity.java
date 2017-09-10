package com.projectbakingapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.projectbakingapp.IdlingResource.SimpleIdlingResource;
import com.projectbakingapp.IngredientWidgetService;
import com.projectbakingapp.R;
import com.projectbakingapp.adapter.ListRecipeAdapter;
import com.projectbakingapp.api.ApiInterface;
import com.projectbakingapp.model.Ingredient;
import com.projectbakingapp.model.Recipe;
import com.projectbakingapp.model.Step;
import com.projectbakingapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,
        ListRecipeAdapter.ListRecipeAdapterListener, DownloadData.DelayerCallback {

    private static final String ADAPTER_TYPE_MAIN = "ADAPTER_TYPE_MAIN";
    private static final int DELAY_MILLIS = 3000;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.list_recipe)
    RecyclerView recyclerView;
    @BindView(R.id.list_recipe_refresh)
    SwipeRefreshLayout refreshListRecipe;

    private ApiInterface apiInterface;
    private List<Recipe> recipeList;
    private ListRecipeAdapter recipeAdapter;

    @Nullable
    private SimpleIdlingResource idlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (idlingResource == null) {
            idlingResource = new SimpleIdlingResource();
        }
        return idlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        getIdlingResource();

        apiInterface = Utils.getAPIService();
        recipeList = new ArrayList<Recipe>();
        recipeAdapter = new ListRecipeAdapter(recipeList, MainActivity.this, MainActivity.this, ADAPTER_TYPE_MAIN);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, 1));
        recyclerView.setAdapter(recipeAdapter);
        refreshListRecipe.setOnRefreshListener(this);

        refreshListRecipe.post(new Runnable() {
            @Override
            public void run() {
                refreshListRecipe.setRefreshing(true);
            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        DownloadData.downloadData(MainActivity.this, MainActivity.this, idlingResource);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        int orientation = newConfig.orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, 1));
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, 1));
        }

    }

    @Override
    public void onRefresh() {
        refreshListRecipe.setRefreshing(true);
        DownloadData.downloadData(MainActivity.this, MainActivity.this, idlingResource);
    }

    @Override
    public void onStepClicked(int position) {
        List<Step> stepList = recipeList.get(position).getSteps();
        Intent intent = new Intent(MainActivity.this, StepActivity.class);
        Gson gson = new Gson();
        String dataIntent = gson.toJson(stepList);
        intent.putExtra(getString(R.string.list_step), dataIntent);
        startActivity(intent);
    }

    @Override
    public void onIngredientsClicked(int position) {
        List<Ingredient> ingredients = recipeList.get(position).getIngredients();
        Intent intent = new Intent(MainActivity.this, IngredientActivity.class);
        Gson gson = new Gson();
        String dataIntent = gson.toJson(ingredients);
        intent.putExtra(getString(R.string.list_ingredient), dataIntent);
        intent.putExtra(getString(R.string.recipe_name), recipeList.get(position).getName());
        startActivity(intent);
    }

    @Override
    public void onDone(ArrayList<Recipe> recipeList) {
        this.recipeList.clear();
        for (Recipe recipe : recipeList) {
            this.recipeList.add(recipe);
        }
        recipeAdapter.notifyDataSetChanged();
        refreshListRecipe.setRefreshing(false);
    }
}


class DownloadData {
    private static final int DELAY_MILLIS = 5000;


    private static ArrayList<Recipe> recipeList = new ArrayList<>();

    interface DelayerCallback {
        void onDone(ArrayList<Recipe> recipeList);
    }

    static void downloadData(Context context, final DelayerCallback callback,
                             @Nullable final SimpleIdlingResource idlingResource) {

        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }

        try {
            ApiInterface apiInterface = Utils.getAPIService();
            apiInterface.getListRecipe().enqueue(new Callback<List<Recipe>>() {
                @Override
                public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                    if (response.isSuccessful()) {
                        recipeList.clear();
                        for (Recipe recipe : response.body()) {
                            recipeList.add(recipe);
                        }

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (callback != null) {
                                    callback.onDone(recipeList);
                                    if (idlingResource != null) {
                                        idlingResource.setIdleState(true);
                                    }
                                }
                            }
                        }, DELAY_MILLIS);

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

}
