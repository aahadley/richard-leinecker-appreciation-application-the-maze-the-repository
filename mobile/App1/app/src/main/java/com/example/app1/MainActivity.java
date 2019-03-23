package com.example.app1;

import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.json.JSONException;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    @Override
    //bundle is when you want to pass in information to this instance window
    protected void onCreate(Bundle savedInstanceState) {
        //calls the onCreate function from the AppCompatActivity SuperClass
        super.onCreate(savedInstanceState);
        //puts widgets on UI
        setContentView(R.layout.activity_main);
        //asks android for permission to these 3 states
        ActivityCompat.requestPermissions(this,
                new String[]{ android.Manifest.permission.ACCESS_WIFI_STATE, android.Manifest.permission.ACCESS_NETWORK_STATE, android.Manifest.permission.INTERNET,
                }, 100);
    }

    @Override
    //the parameters that were passed in tell you which permissions out of the list were granted
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100: {
                //if no permissions were granted, then the length of the grantResults would be zero
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v("tag","granted");
                } else {
                    Log.v("tag", "ACCESS DENIED");
                }
            }
        }
    }

    public void upClick(View view ) {
        Log.v("tag","up was clicked");
    }
    public void downClick(View view ) {
        Log.v("tag","down was clicked");
    }
    public void rightClick(View view ) {
        Log.v("tag","right was clicked");
    }
    public void leftClick(View view ) {
        Log.v("tag", "left was clicked");
    }
    //check this site to make sure that the json is working, the object would appear at the bottom of the screen
    public void testJson(View view) {
        new HTTPAsyncTask().execute("http://hmkcode.appspot.com/jsonservlet");
    }
}
