package com.projectbakingapp.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.projectbakingapp.R;
import com.projectbakingapp.model.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by abdul on 8/27/2017.
 */

public class ListRecipeAdapter extends RecyclerView.Adapter<ListRecipeAdapter.DataViewHolder> {

    private List<Recipe> recipeList;
    private Context context;
    private ListRecipeAdapterListener listener;

    public ListRecipeAdapter(List<Recipe> recipeList, Context context, ListRecipeAdapterListener listener) {
        this.recipeList = recipeList;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_item, parent, false);
        return new ListRecipeAdapter.DataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DataViewHolder holder, final int position) {
        final Recipe recipe = recipeList.get(position);
        holder.recipeName.setText(recipe.getName());
        holder.btnRecipeStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onStepClicked(position);
            }
        });
        holder.btnRecipeIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onIngredientsClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public class DataViewHolder extends RecyclerView.ViewHolder {

        @Nullable @BindView(R.id.recipe_name_item) TextView recipeName;
        @Nullable @BindView(R.id.recipe_step_item) TextView btnRecipeStep;
        @Nullable @BindView(R.id.recipe_ingredients_item) TextView btnRecipeIngredients;

        public DataViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ListRecipeAdapterListener {

        void onStepClicked(int position);

        void onIngredientsClicked(int position);

    }

}
