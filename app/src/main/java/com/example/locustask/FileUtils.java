package com.example.locustask;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {

    private static final String TAG = "FileUtils";

    public static final String IMG_FILE_EXTN = ".jpg";

    /**
     * Delete file with the given path ..
     * @param filePath the file to be deleted
     * @return if the deletion of file was a succes or not.
     */
    public static boolean deleteFile(@NonNull String filePath) {
        try {
            File f = new File(filePath);
            return f.delete();
        } catch (Exception e) {
            Log.e(TAG, "Exception in deleting the file:", e);
            return false;
        }
    }

    public static File getInternalFilesDir() {
        return MyApplication.applicationContext.getFilesDir();
    }

    public static boolean saveBitmapToFile(Bitmap bitmap, String filePath, int compression) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filePath);
            // bmp is your Bitmap instance
            bitmap.compress(Bitmap.CompressFormat.JPEG, compression, out);
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            Log.e(TAG, "Exception in saving the file", e);
            return false;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
