package airnow.demo.ads;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.air.sdk.addons.airx.AirFullscreenListener;
import com.air.sdk.injector.AirFullscreen;

/*
Main screen of the demo app:
- Navigation between integration samples
- Calling Fullscreen ads sample
 */

public class MainActivity extends AppCompatActivity {

    private final static String TAG = MainActivity.class.getName();
    private NetworkConnectivityManager connectivityManager;

    private AirFullscreen airFullscreen;
    private CardView fullscreenCard;
    private TextView fullscreenTitle;
    private TextView fullscreenText;
    private boolean isFullscreenShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        connectivityManager = new NetworkConnectivityManager();
        fullscreenCard = findViewById(R.id.fullscreen);
        fullscreenTitle = findViewById(R.id.fullscreen_title);
        fullscreenText = findViewById(R.id.fullscreen_text);
        enableFullscreenCard(false);
    }

    private void prepareFullscreenAd() {
        //Create AirFullscreen object in your activity, add pass your activity context to the constructor
        airFullscreen = new AirFullscreen(MainActivity.this);

        //Implement the Listener
        //The AirFullscreen sends several events to inform you of Banner activity
        airFullscreen.setAdListener(new AirFullscreenListener() {
            // Invoked when there is no AirFullscreen Ad available after calling load function.
            @Override
            public void onAdFailed(String s) {
                Log.e(TAG, "Fullscreen failed: " + s);
            }

            // Invoked when AirFullscreen Ad is ready to be shown after load function was called.
            @Override
            public void onAdLoaded() {
                enableFullscreenCard(true);
            }

            // Invoked when the Fullscreen Ad is shown
            @Override
            public void onAdShown() {
                isFullscreenShown = true;
                enableFullscreenCard(false);
            }

            // Invoked when the ad is closed and the user is about to return to the application.
            @Override
            public void onAdClosed() {
                if (isFullscreenShown) {
                    isFullscreenShown = false;
                    enableFullscreenCard(false);

                    startActivity(new Intent(MainActivity.this, AfterFullscreenActivity.class));
                }
            }

            @Override
            public void onAdClicked() {
                isFullscreenShown = false;
                enableFullscreenCard(false);
            }

            @Override
            public void onLeaveApplication() {
            }
        });

        Log.e(TAG, "loadAd");
        airFullscreen.loadAd();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (connectivityManager.getConnectivityStatus(this) ==
                NetworkConnectivityManager.TYPE_NOT_CONNECTED) {
            connectivityManager.subscribeOnConnectionAvailable(this, new Runnable() {
                @Override
                public void run() {
                    prepareFullscreenAd();
                    connectivityManager.unregister(MainActivity.this);
                }
            });

            return;
        }

        prepareFullscreenAd();
    }

    @Override
    protected void onPause() {
        super.onPause();
        connectivityManager.unregister(MainActivity.this);
    }


    private void enableFullscreenCard(boolean enabled) {
        fullscreenCard.setEnabled(enabled);
        if (enabled) {
            fullscreenTitle.setTextColor(getResources().getColor(R.color.text_color));
            fullscreenText.setTextColor(getResources().getColor(R.color.text_color));
        } else {
            fullscreenTitle.setTextColor(getResources().getColor(R.color.text_color_disabled));
            fullscreenText.setTextColor(getResources().getColor(R.color.text_color_disabled));
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.banner_overlay:
                startActivity(new Intent(this, BannerOverlayActivity.class));
                break;
            case R.id.banner_in_feed:
                startActivity(new Intent(this, BannerInFeedActivity.class));
                break;
            case R.id.fullscreen:
                //Once you receive the onAdLoaded callback, you are ready to show an fullscreen
                // ad to your users. Invoke the following method to serve an ad to your users:
                airFullscreen.showAd();
                break;
        }
    }
}
