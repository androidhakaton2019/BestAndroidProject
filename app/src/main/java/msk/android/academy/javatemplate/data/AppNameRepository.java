package msk.android.academy.javatemplate.data;

import android.arch.lifecycle.LiveData;
import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;

import msk.android.academy.javatemplate.AppExecutors;
import msk.android.academy.javatemplate.data.network.AppNameNetworkDataSource;

public class AppNameRepository {

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static AppNameRepository sInstance;
    private final AppNameNetworkDataSource mAppNameNetworkDataSource;
    private final AppExecutors mExecutors;

    private AppNameRepository(AppNameNetworkDataSource AppNameNetworkDataSource,
                              AppExecutors executors) {
        mAppNameNetworkDataSource = AppNameNetworkDataSource;
        mExecutors = executors;
    }

    public synchronized static AppNameRepository getInstance(
            AppNameNetworkDataSource AppNameNetworkDataSource,
            AppExecutors executors) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new AppNameRepository(AppNameNetworkDataSource,
                        executors);
            }
        }
        return sInstance;
    }

    public void setFirebaseUser(FirebaseUser firebaseUser) {
        mAppNameNetworkDataSource.setFirebaseUser(firebaseUser);
    }

    public LiveData<Uri> getUserPhotoUri() {
        return mAppNameNetworkDataSource.getUserPhotoUri();
    }

    public LiveData<String> getUserName() {
        return mAppNameNetworkDataSource.getUserName();
    }

    public LiveData<String> getUserEmail() {
        return mAppNameNetworkDataSource.getUserEmail();
    }

}
