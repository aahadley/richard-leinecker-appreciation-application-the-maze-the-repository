package com.example.therichardleineckerappreciationappthemaze;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.therichardleineckerappreciationappthemaze.constants;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity {
    public String PrimaryMessage;
    long previousActionTime;
    long currentActionTime;
    long remainingActionCooldown;
    AdView mAdView;
    AdRequest adRequest;
    TextView tv01;
    TextView tv02;
//public TextView textElement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this,
                new String[]{ android.Manifest.permission.ACCESS_WIFI_STATE,
                        android.Manifest.permission.ACCESS_NETWORK_STATE,
                        android.Manifest.permission.INTERNET,
                }, 100);

        this.PrimaryMessage = constants.testMessage;
        this.previousActionTime = 0;
        this.remainingActionCooldown = 0;

        this.mAdView = (AdView) findViewById(R.id.adView);
        this.adRequest = new AdRequest.Builder().build();

        this.mAdView.loadAd(this.adRequest);
        this.tv01 = (TextView)findViewById(R.id.textView01);
        this.tv01.setText(this.PrimaryMessage);
        this.tv02 = (TextView)findViewById(R.id.textView02);
        this.tv02.setText(constants.gameURL);
    }
    @Override
    //the parameters that were passed in tell you which permissions out of the list were granted
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100: {
                //if no permissions were granted, then the length of the grantResults would be zero
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v("tag","granted");
                    this.PrimaryMessage = constants.networkSuccess;
                }
                else {
                    Log.v("tag", "ACCESS DENIED");
                    this.PrimaryMessage = constants.networkFailure;
                }
            }
        }
    }

    public void upClick(View view ) {
        Log.v("tag","up was clicked");
        this.currentActionTime = System.currentTimeMillis();
        if((this.currentActionTime - this.previousActionTime) < (constants.actionCoolDown)) {
            this.remainingActionCooldown = ((constants.actionCoolDown) - (this.currentActionTime - this.previousActionTime))/1000;
            this.tv01.setText(constants.slowDownPartA+this.remainingActionCooldown+constants.slowDownPartB);
        }
        else {
            this.previousActionTime = this.currentActionTime;
            this.tv01.setText(constants.upMessage);
        }
    }
    public void downClick(View view ) {
        Log.v("tag","down was clicked");
        this.currentActionTime = System.currentTimeMillis();
        if((this.currentActionTime - this.previousActionTime) < (constants.actionCoolDown)) {
            this.remainingActionCooldown = ((constants.actionCoolDown) - (this.currentActionTime - this.previousActionTime))/1000;
            this.tv01.setText(constants.slowDownPartA+this.remainingActionCooldown+constants.slowDownPartB);
        }
        else {
            this.previousActionTime = this.currentActionTime;
            this.tv01.setText(constants.downMessage);
        }
    }
    public void rightClick(View view ) {
        Log.v("tag","right was clicked");
        this.currentActionTime = System.currentTimeMillis();
        if((this.currentActionTime - this.previousActionTime) < (constants.actionCoolDown)) {
            this.remainingActionCooldown = ((constants.actionCoolDown) - (this.currentActionTime - this.previousActionTime))/1000;
            this.tv01.setText(constants.slowDownPartA+this.remainingActionCooldown+constants.slowDownPartB);
        }
        else {
            this.previousActionTime = this.currentActionTime;
            this.tv01.setText(constants.rightMessage);
        }
    }
    public void leftClick(View view ) {
        Log.v("tag", "left was clicked");
        this.currentActionTime = System.currentTimeMillis();
        if((this.currentActionTime - this.previousActionTime) < (constants.actionCoolDown)) {
            this.remainingActionCooldown = ((constants.actionCoolDown) - (this.currentActionTime - this.previousActionTime))/1000;
            this.tv01.setText(constants.slowDownPartA+this.remainingActionCooldown+constants.slowDownPartB);
        }
        else {
            this.previousActionTime = this.currentActionTime;
            this.tv01.setText(constants.leftMessage);
        }
    }

    public void testJson(View view) {
        new HTTPAsyncTask().execute("http://hmkcode.appspot.com/jsonservlet");
    }
}
