package airnow.demo.ads;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.air.sdk.addons.airx.AirBannerListener;
import com.air.sdk.injector.AirBanner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
Showing AirADS Banner as infeed native ad sample
- Creating demo list of items
- Item class is declared in FeedItem
 */

public class BannerInFeedActivity extends AppCompatActivity {
    private final static String TAG = BannerInFeedActivity.class.getName();
    private final static int FEED_SIZE = 10;
    LinearLayout scrollLayout;
    private NetworkConnectivityManager connectivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_in_feed);

        connectivityManager = new NetworkConnectivityManager();

        scrollLayout = findViewById(R.id.scrollLayout);
        populateScrollView(getRandomizedFeedList());


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
                try {
                    int randomPosition = 3;
                    View divider = new View(BannerInFeedActivity.this);
                    divider.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
                    divider.setBackgroundColor(getResources().getColor(R.color.text_color));
                    View bannerView = getLayoutInflater().inflate(R.layout.item_banner, scrollLayout, false);
                    ((FrameLayout) bannerView.findViewById(R.id.banner)).removeAllViews();
                    ((FrameLayout) bannerView.findViewById(R.id.banner)).addView(view);
                    scrollLayout.addView(bannerView, randomPosition);
                    scrollLayout.addView(divider, randomPosition);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
                    airBanner.loadAd(350, 150);
                    connectivityManager.unregister(BannerInFeedActivity.this);
                }
            });

            return;
        }

        //Load Banner ad
        airBanner.loadAd(350, 150);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_banner_in_feed);
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

    private List<FeedItem> getRandomizedFeedList() {
        ArrayList<FeedItem> list = new ArrayList<>();

        for (int i = 0; i < FEED_SIZE; i++) {
            int j = new Random().nextInt(3);
            FeedItem item;
            switch (j) {
                case 0:
                    item = new FeedItem(R.drawable.ic_news_item1,
                            getString(R.string.feed_item_title1),
                            getString(R.string.feed_item_text1));
                    break;
                case 1:
                    item = new FeedItem(R.drawable.ic_news_item2,
                            getString(R.string.feed_item_title2),
                            getString(R.string.feed_item_text2));
                    break;
                default:
                case 2:
                    item = new FeedItem(R.drawable.ic_news_item3,
                            getString(R.string.feed_item_title3),
                            getString(R.string.feed_item_text3));
                    break;
            }

            list.add(item);
        }

        return list;
    }

    private void populateScrollView(List<FeedItem> items) {


        for (FeedItem item : items) {
            View divider = new View(this);
            divider.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
            divider.setBackgroundColor(getResources().getColor(R.color.text_color));
            View itemView = getLayoutInflater().inflate(R.layout.item_feed, scrollLayout, false);
            ImageView icon = itemView.findViewById(R.id.icon_feed);
            TextView title = itemView.findViewById(R.id.title_feed);
            TextView text = itemView.findViewById(R.id.text_feed);
            icon.setImageResource(item.getDrawableRes());
            title.setText(item.getTitle());
            text.setText(item.getText());

            scrollLayout.addView(itemView);
            scrollLayout.addView(divider);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        connectivityManager.unregister(BannerInFeedActivity.this);
    }
}
