package com.example.locustask;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;

import com.example.locustask.form.FormFragment;
import com.example.locustask.form.FullImageViewFragment;

public class MainActivity extends AppCompatActivity implements FormFragment.OnFragmentInteractionListener {

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private FormFragment mFrag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupFragment();
    }

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mybutton) {
            mFrag.printDetails();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupFragment() {
        mFrag = FormFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentHolder, mFrag).commit();
    }

    @Override
    public void launchCameraForPhoto() {
        dispatchTakePictureIntent();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mFrag.saveImageBitmap(imageBitmap);
        }
    }

    @Override
    public void openEnlargedPicture(String filePath) {

        FullImageViewFragment frag = FullImageViewFragment.newInstance(filePath);
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragmentHolder, frag)
                .commit();

    }
}
