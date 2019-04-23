package my.app.therichardleineckerappreciationappthemaze;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.CollapsibleActionView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity {
    public String PrimaryMessage;
    public static String command;
    long previousActionTime;
    long currentActionTime;
    long remainingActionCooldown;
    public AdView adView;
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

        Bundle extras = new Bundle();
        extras.putString("max_ad_content_rating", "PG");
        AdRequest adRequest = new AdRequest.Builder()
                .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                .build();
        this.adView = (AdView) findViewById(R.id.adView);
        this.adView.loadAd(adRequest);

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

    public void checkNetworkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        boolean isConnected = false;
        if (networkInfo != null && (isConnected = networkInfo.isConnected())) {
            tv01.setText(constants.networkSuccess);
        }
        else {
            tv01.setText(constants.networkFailure);
        }

        return;
    }

    public void upClick(View view ) {
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
            this.tv01.setText(constants.slowDown);
        }
        else {
            this.previousActionTime = this.currentActionTime;
            new HTTPAsyncTask().execute();
            this.tv01.setText(constants.upMessage);
        }
    }
    public void downClick(View view ) {
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
            this.tv01.setText(constants.slowDown);
        }
        else {
            this.previousActionTime = this.currentActionTime;
            new HTTPAsyncTask().execute();
            this.tv01.setText(constants.downMessage);
        }
    }
    public void rightClick(View view ) {
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
            this.tv01.setText(constants.slowDown);
        }
        else {
            this.previousActionTime = this.currentActionTime;
            new HTTPAsyncTask().execute();
            this.tv01.setText(constants.rightMessage);
        }
    }
    public void leftClick(View view ) {
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
            this.tv01.setText(constants.slowDown);
        }
        else {
            this.previousActionTime = this.currentActionTime;
            new HTTPAsyncTask().execute();
            this.tv01.setText(constants.leftMessage);
        }
    }

    public void adClick() {
        this.adClicked = true;
    }
}