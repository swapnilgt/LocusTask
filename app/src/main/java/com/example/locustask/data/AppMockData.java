package com.example.locustask.data;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.locustask.AppExecutors;
import com.example.locustask.FileUtils;
import com.example.locustask.data.pojo.FormItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class AppMockData implements AppDataRepo {

    private static final String TAG = "AppMockData";

    private static AppMockData INSTANCE;
    private static final Object LOCK = new Object();

    private Gson mGson;

    private AppMockData() {
        mGson = new Gson();
    }

    private <T> T getObjectFromJsonString(@NonNull String jsonString, @NonNull Type type) {
        return mGson.fromJson(jsonString, type);
    }

    private <T> T getObjectFromJsonFile(@NonNull Context context, Type type) {

        String json  = null;
        try {
            InputStream is = context.getAssets().open("listdata.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        Log.d(TAG, "The json data laoded is: " + json);
        return getObjectFromJsonString(json, type);
    }

    public static AppMockData getInstance() {
        if(INSTANCE == null) {
            synchronized (LOCK) {
                if(INSTANCE == null) {
                    INSTANCE = new AppMockData();
                }
            }
        }

        return INSTANCE;
    }

    @Override
    public void loadFromItems(@NonNull Context context, @NonNull GetFormData callback) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            List<FormItem> items = getObjectFromJsonFile(context, new TypeToken<List<FormItem>>(){}.getType());
            // Iterating and populating data for the list ...
            for(FormItem item: items) {
                item.setPicFilePath(FileUtils.getInternalFilesDir() + File.separator + item.getId() + FileUtils.IMG_FILE_EXTN);
                item.setTypeInt(); // This is set based on the type string ...
            }
            AppExecutors.getInstance().mainThread().execute(() -> {
                callback.onLoad(items, null);
            });
        });
    }
}
