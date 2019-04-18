package com.example.therichardleineckerappreciationappthemaze;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.therichardleineckerappreciationappthemaze.constants;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity {
    public String PrimaryMessage;
    public static String command;
    long previousActionTime;
    long currentActionTime;
    long remainingActionCooldown;
    private AdView adView;
    AdRequest adRequest;
    static TextView tv01;
    static TextView tv02;
    public boolean adClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this,
                new String[]{ android.Manifest.permission.ACCESS_WIFI_STATE,
                        android.Manifest.permission.ACCESS_NETWORK_STATE,
                        android.Manifest.permission.INTERNET,
                }, 100);

        this.PrimaryMessage = constants.defaultMessage;
        this.previousActionTime = 0;
        this.remainingActionCooldown = 0;

        this.adView = (AdView) findViewById(R.id.adView);
        this.adRequest = new AdRequest.Builder().build();
        this.adView.loadAd(this.adRequest);

        com.google.android.gms.ads.AdListener adListener = new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                adClick();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }
        };
        this.adView.setAdListener(adListener);

        this.tv01 = (TextView)findViewById(R.id.textView01);
        this.tv01.setText(this.PrimaryMessage);
        this.tv02 = (TextView)findViewById(R.id.textView02);
        this.tv02.setText(constants.gameURL);

        checkNetworkConnection();
    }

    public boolean checkNetworkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        boolean isConnected = false;
        if (networkInfo != null && (isConnected = networkInfo.isConnected())) {
            // show "Connected" & type of network "WIFI or MOBILE"
            tv01.setText(constants.networkSuccess);
        }
        else {
            // show "Not Connected"
            tv01.setText(constants.networkFailure);
        }

        return isConnected;
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
        this.command = constants.up;
        if(this.adClicked){
            this.adClicked = false;
            this.previousActionTime = this.currentActionTime;
            new HTTPAsyncTask().execute();
            this.tv01.setText(constants.upMessage);
        }
        else if((this.currentActionTime - this.previousActionTime) < (constants.actionCoolDown)) {
            this.remainingActionCooldown = ((constants.actionCoolDown) - (this.currentActionTime - this.previousActionTime))/1000;
            this.tv01.setText(constants.slowDownPartA+this.remainingActionCooldown+constants.slowDownPartB);
        }
        else {
            this.previousActionTime = this.currentActionTime;
            new HTTPAsyncTask().execute();
            this.tv01.setText(constants.upMessage);
        }
    }
    public void downClick(View view ) {
        Log.v("tag","down was clicked");
        this.currentActionTime = System.currentTimeMillis();
        this.command = constants.down;
        if(this.adClicked){
            this.adClicked = false;
            this.previousActionTime = this.currentActionTime;
            new HTTPAsyncTask().execute();
            this.tv01.setText(constants.downMessage);
        }
        else if((this.currentActionTime - this.previousActionTime) < (constants.actionCoolDown)) {
            this.remainingActionCooldown = ((constants.actionCoolDown) - (this.currentActionTime - this.previousActionTime))/1000;
            this.tv01.setText(constants.slowDownPartA+this.remainingActionCooldown+constants.slowDownPartB);
        }
        else {
            this.previousActionTime = this.currentActionTime;
            new HTTPAsyncTask().execute();
            this.tv01.setText(constants.downMessage);
        }
    }
    public void rightClick(View view ) {
        Log.v("tag","right was clicked");
        this.currentActionTime = System.currentTimeMillis();
        this.command = constants.right;
        if(this.adClicked){
            this.adClicked = false;
            this.previousActionTime = this.currentActionTime;
            new HTTPAsyncTask().execute();
            this.tv01.setText(constants.rightMessage);
        }
        else if((this.currentActionTime - this.previousActionTime) < (constants.actionCoolDown)) {
            this.remainingActionCooldown = ((constants.actionCoolDown) - (this.currentActionTime - this.previousActionTime))/1000;
            this.tv01.setText(constants.slowDownPartA+this.remainingActionCooldown+constants.slowDownPartB);
        }
        else {
            this.previousActionTime = this.currentActionTime;
            new HTTPAsyncTask().execute();
            this.tv01.setText(constants.rightMessage);
        }
    }
    public void leftClick(View view ) {
        Log.v("tag", "left was clicked");
        this.currentActionTime = System.currentTimeMillis();
        this.command = constants.left;
        if(this.adClicked){
            this.adClicked = false;
            this.previousActionTime = this.currentActionTime;
            new HTTPAsyncTask().execute();
            this.tv01.setText(constants.leftMessage);
        }
        else if((this.currentActionTime - this.previousActionTime) < (constants.actionCoolDown)) {
            this.remainingActionCooldown = ((constants.actionCoolDown) - (this.currentActionTime - this.previousActionTime))/1000;
            this.tv01.setText(constants.slowDownPartA+this.remainingActionCooldown+constants.slowDownPartB);
        }
        else {
            this.previousActionTime = this.currentActionTime;
            new HTTPAsyncTask().execute();
            this.tv01.setText(constants.leftMessage);
        }
    }

    public void adClick() {
        Log.v("tag", "ad was clicked");
        this.adClicked = true;
    }
}
