package com.project.Pictionary;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

public class AppUtils {

    private static AppUtils instance;

    public static AppUtils getInstance(){
        if(instance == null){
            instance = new AppUtils();
        }
        return instance;
    }

    public String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("pictionary.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
