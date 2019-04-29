package com.example.locustask.data;

import android.content.Context;

import com.example.locustask.data.pojo.FormItem;

import java.util.List;

public interface AppDataRepo {

    interface GetFormData {
        void onLoad(List<FormItem> list, Exception exp);
    }

    void loadFromItems(Context context, GetFormData callback);
}
