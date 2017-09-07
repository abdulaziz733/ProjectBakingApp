package com.projectbakingapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.projectbakingapp.IngredientWidgetService;
import com.projectbakingapp.R;
import com.projectbakingapp.adapter.ListIngredientAdapter;
import com.projectbakingapp.data.TempData;
import com.projectbakingapp.model.Ingredient;
import com.projectbakingapp.model.Step;
import com.projectbakingapp.utils.SharedPreferencesUtils;
import com.projectbakingapp.utils.Utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.list_ingredient)
    RecyclerView recyclerView;

    private Intent intent;
    private ListIngredientAdapter adapter;
    private ArrayList<Ingredient> ingredientList;
    private String recipeName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ingredientList = new ArrayList<>();
        intent = getIntent();
        if (intent.hasExtra(getString(R.string.list_ingredient))){
            Type listType = new TypeToken<List<Ingredient>>(){}.getType();
            String resultData = intent.getStringExtra(getString(R.string.list_ingredient));
            recipeName = intent.getStringExtra(getString(R.string.recipe_name));
            Gson gson = new Gson();
            ingredientList = gson.fromJson(resultData, listType);
            recyclerView.setLayoutManager(new LinearLayoutManager(IngredientActivity.this));
            adapter = new ListIngredientAdapter(ingredientList, IngredientActivity.this);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            String choosenIngredient = gson.toJson(ingredientList);
            Utils.saveDataIngredient(IngredientActivity.this, ingredientList, recipeName);
            IngredientWidgetService.startActionUpdateIngWidgets(IngredientActivity.this);

        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
