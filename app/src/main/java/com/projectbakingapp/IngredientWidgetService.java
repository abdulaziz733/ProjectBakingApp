package com.projectbakingapp;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
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

public class IngredientWidgetService extends IntentService {

    public static final String ACTION_UPDATE_LIST_ING = "android.appwidget.action.APPWIDGET_UPDATE";

    public IngredientWidgetService() {
        super("IngredientWidgetService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_LIST_ING.equals(action)) {
                handleActionUpdateIngradientWidgets();
            }
        }
    }

    public static void startActionUpdateIngWidgets(Context context) {
        Intent intent = new Intent(context, IngredientWidgetService.class);
        intent.setAction(ACTION_UPDATE_LIST_ING);
        context.startService(intent);
    }

    private void handleActionUpdateIngradientWidgets() {

        Intent intent = new Intent(ACTION_UPDATE_LIST_ING);
        intent.setAction(ACTION_UPDATE_LIST_ING);
        sendBroadcast(intent);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, IngredientsWidgetProvider.class));

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.ingredients_widget_provider);

        IngredientsWidgetProvider.updateIngredientWidgets(this, appWidgetManager, appWidgetIds);


    }

}
