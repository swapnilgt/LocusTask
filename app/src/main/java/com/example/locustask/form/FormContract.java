package com.example.locustask.form;

import android.graphics.Bitmap;

import com.example.locustask.data.pojo.FormItem;

import java.util.List;

public interface FormContract {

    interface Presenter {

        void loadData();

        // Methods for photo ...
        void clickPhotoPlaceholder(String itemId, int position);

        void clickDeletePhoto(String itemId, int position);

        // Methods for the comments section ..

        void onClickSubmit();

        void updateComment(String comment, int position);

        void toggleComment(boolean newState, int position);

        void updateSelectedIndex(int newSelectedIndex, int position);

        void saveImageTaken(Bitmap bitmap);

        void printDetails();

    }

    interface View {

        void openEnlargedPhoto(String filePath);

        void launchCameraForClickingPhoto();

        void setList(List<FormItem> list);

        void updateItemInList(int position);
    }
}
