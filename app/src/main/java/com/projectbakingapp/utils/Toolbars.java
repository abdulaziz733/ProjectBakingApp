package com.projectbakingapp.utils;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by widyan on 3/22/2017.
 */

public class Toolbars {
    private AppCompatActivity activity;
    private Toolbar toolbar;

    public Toolbars(AppCompatActivity activity, Toolbar toolbar) {
        this.activity = activity;
        this.toolbar = toolbar;
    }

    public void setToolbar(){
        activity.setSupportActionBar(toolbar);
    }

    public void setToolbar(boolean backhome){
        if(backhome){
            activity.setSupportActionBar(toolbar);
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


}
