package com.projectbakingapp.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.projectbakingapp.R;
import com.projectbakingapp.model.Ingredient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by abdul on 8/28/2017.
 */

public class ListIngredientAdapter extends RecyclerView.Adapter<ListIngredientAdapter.DataViewHolder> {

    private List<Ingredient> ingredientList;
    private Context context;

    public ListIngredientAdapter(List<Ingredient> ingredientList, Context context) {
        this.ingredientList = ingredientList;
        this.context = context;
    }

    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredients_item, parent, false);
        return new ListIngredientAdapter.DataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DataViewHolder holder, int position) {
        Ingredient ingredient = ingredientList.get(position);
        holder.ingredient_name.setText(ingredient.getIngredient());
        holder.ingredient_quantity.setText(ingredient.getQuantity() + "");
        holder.ingredient_measure.setText(ingredient.getMeasure());
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    public class DataViewHolder extends RecyclerView.ViewHolder {

        @Nullable @BindView(R.id.ingredient_name_item) TextView ingredient_name;
        @Nullable @BindView(R.id.quantity_item) TextView ingredient_quantity;
        @Nullable @BindView(R.id.measure_item) TextView ingredient_measure;

        public DataViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
