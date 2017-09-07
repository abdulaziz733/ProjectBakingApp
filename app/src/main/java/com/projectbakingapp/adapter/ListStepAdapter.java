package com.projectbakingapp.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.projectbakingapp.R;
import com.projectbakingapp.model.Step;

import java.security.PrivateKey;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by abdul on 8/27/2017.
 */

public class ListStepAdapter extends RecyclerView.Adapter<ListStepAdapter.DataViewHolder> {

    private List<Step> stepList;
    private Context context;
    private OnStepChooseListener listener;

    public ListStepAdapter(List<Step> stepList, Context context, OnStepChooseListener listener) {
        this.stepList = stepList;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.step_item, parent, false);
        return new ListStepAdapter.DataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DataViewHolder holder, final int position) {
        Step step = stepList.get(position);
//        holder.stepName.setText(context.getString(R.string.step) + (position + 1));
        holder.stepName.setText(step.getShortDescription());
        holder.stepView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.OnStepChooseListener(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stepList.size();
    }

    public class DataViewHolder extends RecyclerView.ViewHolder {

        @Nullable @BindView(R.id.step_name_item) TextView stepName;
        @Nullable @BindView(R.id.step_view) LinearLayout stepView;


        public DataViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnStepChooseListener{
        void OnStepChooseListener(int position);
    }

}
