package airnow.demo.ads;

import android.app.Application;
import android.os.Build;

import com.air.sdk.injector.AirpushSdk;

/*
AirSDK initialization sample
 */

//Don't forget to add your class name to AndroidManifest inside the <application> tag
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //AirSDK initialization
        AirpushSdk.init(this);
    }
}
