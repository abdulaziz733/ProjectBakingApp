package com.projectbakingapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projectbakingapp.R;
import com.projectbakingapp.adapter.ListStepAdapter;
import com.projectbakingapp.model.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by abdul on 8/27/2017.
 */

public class ListStepFragment extends Fragment implements ListStepAdapter.OnStepChooseListener {

    private List<Step> stepList;

    @BindView(R.id.rv_list_step) RecyclerView recyclerView;
    private ListStepAdapter listStepAdapter;
    private OnStepClickListener mCallBack;

    public ListStepFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallBack = (OnStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnStepClickListener");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_list_step, container, false);
        ButterKnife.bind(this, rootView);

        if (stepList != null){
            listStepAdapter = new ListStepAdapter(stepList, getActivity(), ListStepFragment.this);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(listStepAdapter);
            listStepAdapter.notifyDataSetChanged();


        }


        return rootView;
    }

    public void setStepList(List<Step> stepList) {
        this.stepList =stepList;

    }

    @Override
    public void OnStepChooseListener(int position) {
        mCallBack.nnStepClickListener(position);
    }

    public interface OnStepClickListener {
        void nnStepClickListener(int position);
    }
}
