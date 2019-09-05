package airnow.demo.ads;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.air.sdk.addons.airx.AirBannerListener;
import com.air.sdk.injector.AirBanner;

/*
Showing AirADS Banner as overlay sample
- Creating demo feed
-
 */

public class BannerOverlayActivity extends AppCompatActivity {
    private final static String TAG = BannerOverlayActivity.class.getName();
    private NetworkConnectivityManager connectivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_overlay);

        connectivityManager = new NetworkConnectivityManager();

        //Create AirBanner object in your activity, add pass your activity context to the constructor
        final AirBanner airBanner = new AirBanner(this);

        //Implement the Listener
        //The AirSDK sends several events to inform you of Banner activity
        airBanner.setAdListener(new AirBannerListener() {

            // Invoked when there is no Banner Ad available after calling load function.
            @Override
            public void onAdFailed(String s) {
                Log.e(TAG, "Banner failed: " + s);
            }

            // Invoked when Banner Ad is ready to be shown after load function was called.
            @Override
            public void onAdLoaded(View view) {

                //Find the banner in the layout and clear previous creative
                ((FrameLayout) findViewById(R.id.banner)).removeAllViews();

                //Find the banner in the layout and show loaded creative in it
                ((FrameLayout) findViewById(R.id.banner)).addView(view);
            }

            // Invoked when the ad is closed and the user is about to return to the application.
            @Override
            public void onAdClosed() {
            }

            // Invoked when the end user clicked on the Banner ad.
            @Override
            public void onAdClicked() {
            }

            // Invoked when the user is about to leave the application.
            @Override
            public void onLeaveApplication() {
            }
        });


        if (connectivityManager.getConnectivityStatus(this) ==
                NetworkConnectivityManager.TYPE_NOT_CONNECTED) {
            connectivityManager.subscribeOnConnectionAvailable(this, new Runnable() {
                @Override
                public void run() {
                    //Load Banner ad
                    airBanner.loadAd(AirBanner.BANNER);
                    connectivityManager.unregister(BannerOverlayActivity.this);
                }
            });

            return;
        }

        //Load Banner ad
        airBanner.loadAd(AirBanner.BANNER);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_banner_overlay);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
