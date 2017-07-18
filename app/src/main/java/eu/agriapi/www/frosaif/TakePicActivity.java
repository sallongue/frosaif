package eu.agriapi.www.frosaif;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;

/**
 * Created by galadriel on 28/06/2017.
 */

public class TakePicActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 0;
    final int PHOTO_RESULT = 1;
    private Uri mLAstPhotoUri = null;
    ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_takepic);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    private Uri createFileUri() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").
                format(System.currentTimeMillis());
        String fileName = "PHOTO_" + timeStamp + ".jpg";
        return Uri.fromFile(new File(Environment.
                getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), fileName));
    }

    public void takePicture(View view) {
        Intent takePicIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePicIntent.resolveActivity(getPackageManager()) != null) {
            mLAstPhotoUri = createFileUri();
            Log.d("++++++++++", mLAstPhotoUri.toString());
            takePicIntent.putExtra(MediaStore.EXTRA_OUTPUT, mLAstPhotoUri);
            startActivityForResult(takePicIntent, PHOTO_RESULT);
        }
        Log.d("----------------", "par la");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent Data) {
        mImageView = (ImageView) findViewById(R.id.imgTakePic);

        if (requestCode == PHOTO_RESULT && resultCode == RESULT_OK) {
            mImageView.setImageBitmap(BitmapFactory.decodeFile(mLAstPhotoUri.getPath()));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.d("********************", "permission granted");

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getApplicationContext(), R.string.permReadNG, Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra("imagePath", mLAstPhotoUri.getPath());
        // Activity finished ok, return the data
        setResult(RESULT_OK, data);
        super.finish();
    }
}