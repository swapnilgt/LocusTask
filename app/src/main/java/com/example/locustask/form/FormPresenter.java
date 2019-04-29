package com.example.locustask.form;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.locustask.AppExecutors;
import com.example.locustask.FileUtils;
import com.example.locustask.MyApplication;
import com.example.locustask.data.AppDataRepo;
import com.example.locustask.data.pojo.FormItem;

import java.lang.ref.WeakReference;
import java.util.List;

public class FormPresenter implements FormContract.Presenter {

    private static final String TAG = "FormPresenter";

    private final WeakReference<FormContract.View> mView;
    private final AppDataRepo mRepo;

    List<FormItem> mList;

    private int mRequestedPicIndex;


    FormPresenter(FormContract.View mView, AppDataRepo mRepo) {
        this.mView = new WeakReference<>(mView);
        this.mRepo = mRepo;
    }

    @Override
    public void loadData() {
        mRepo.loadFromItems(MyApplication.applicationContext, (list, exp) -> {
            if(exp == null) {
                if(mView.get() != null) {
                    mView.get().setList(list);
                    mList = list;
                }
            } else {
                // TODO - Handle exception accordingly...
                Log.e(TAG, "Exception in loading the form items", exp);
;            }
        });
    }

    @Override
    public void clickPhotoPlaceholder(String itemId, int position) {
        final FormItem item = mList.get(position);
        Log.d(TAG, "Taking the image action for position: " + position);
        if(mView.get() != null) {
            if(item.isImageExists()) {
                Log.d(TAG, "The image exists ...");
                mView.get().openEnlargedPhoto(item.getPicFilePath());
            } else {
                Log.d(TAG, "The image does not exist ...");
                mRequestedPicIndex = position;
                mView.get().launchCameraForClickingPhoto();
            }
        }
    }

    @Override
    public void clickDeletePhoto(String itemId, int position) {
        final FormItem item = mList.get(position);
        FileUtils.deleteFile(item.getPicFilePath());
        Log.d(TAG, "Making the image exists for item as index " + mRequestedPicIndex + " true");
        item.setImageExists(false);
        if(mView.get() != null) {
            mView.get().updateItemInList(position);
        }
    }

    @Override
    public void onClickSubmit() {
        // TODO -  Work on this ...
    }

    @Override
    public void updateComment(String comment, int position) {
        final FormItem item = mList.get(position);
        item.setComment(comment);
    }

    @Override
    public void toggleComment(boolean newState, int position) {
        final FormItem item = mList.get(position);
        item.setCommentActive(newState);
        Log.d(TAG, "@toggleComment: Call to update index: " + position);
    }

    @Override
    public void updateSelectedIndex(int newSelectedIndex, int position) {
        final FormItem item = mList.get(position);
        item.setChoiceIndex(newSelectedIndex);
    }

    @Override
    public void saveImageTaken(Bitmap bitmap) {
        final FormItem item = mList.get(mRequestedPicIndex);
        AppExecutors.getInstance().diskIO().execute(() -> {
            if(FileUtils.saveBitmapToFile(bitmap, item.getPicFilePath(), 100)) {
                Log.d(TAG, "Making the image exists for item as index " + mRequestedPicIndex + " true");
                item.setImageExists(true);
                AppExecutors.getInstance().mainThread().execute(() -> {
                    if(mView.get() != null) {
                        mView.get().updateItemInList(mRequestedPicIndex);
                    }
                });
            }
        });
    }

    @Override
    public void printDetails() {
        Log.d(TAG, "The details are: " + mList.toString());
    }
}
