package eu.agriapi.www.frosaif;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


/**
 * Created by galadriel on 24/06/2017.
 */

public class FrosaifSplScr extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
        finish();
    }
}
