package com.projectbakingapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Abdurrahman on 10-Dec-16.
 */

public class SharedPreferencesUtils {
    private SharedPreferences sharedPreferences;

    private String iv;

    public SharedPreferencesUtils(Context context, String preferencesName) {
        sharedPreferences = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
    }

    public void storeData(String key, String data){
        try {

            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString(key, data);

            editor.apply();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean checkIfDataExists(String key){
        return sharedPreferences.contains(key);
    }

    public String getPreferenceData(String key) throws Exception {
        String data = sharedPreferences.getString(key, null);
        return data;
    }

    public void removeData(String key){
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (checkIfDataExists(key)) {
            editor.remove(key);

            editor.apply();
        }
    }

    public void clearAllData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();
        editor.apply();
    }
}
