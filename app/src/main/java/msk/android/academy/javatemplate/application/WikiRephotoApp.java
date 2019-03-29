package msk.android.academy.javatemplate.application;

import android.app.Application;
import android.util.Log;

import com.here.mobility.sdk.core.MobilitySdk;

import msk.android.academy.javatemplate.R;
import msk.android.academy.javatemplate.util.AuthUtils;

public class WikiRephotoApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        MobilitySdk.init(this);
        if (MobilitySdk.getInstance().isHereAgentProcess()){
            return;
        }
        Log.d("doron ", "****************");

        AuthUtils.registerUser("stamUser",
                getString(R.string.here_sdk_app_id),
                getString(R.string.here_sdk_app_secret));

    }

    @Override
    public void onTerminate() {
        MobilitySdk.getInstance().setUserAuthInfo(null);

        super.onTerminate();
    }
}
