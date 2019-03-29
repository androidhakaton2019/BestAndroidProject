package msk.android.academy.javatemplate.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

import msk.android.academy.javatemplate.AppExecutors;
import msk.android.academy.javatemplate.data.local.AppNameLocalDataRepository;
import msk.android.academy.javatemplate.data.network.AppNameNetworkDataSource;

public class AppNameRepository {

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static AppNameRepository sInstance;
    private final AppNameNetworkDataSource mAppNameNetworkDataSource;
    private final AppNameLocalDataRepository mAppNameLocalDataRepository;
    private final AppExecutors mExecutors;

    private Map<Integer, OnDataRequestCallback> mRequests;

    private int requestCounter = 0;

    private AppNameRepository(AppNameNetworkDataSource appNameNetworkDataSource,
                              AppNameLocalDataRepository appNameLocalDataRepository,
                              AppExecutors executors) {
        mAppNameNetworkDataSource = appNameNetworkDataSource;
        mAppNameLocalDataRepository = appNameLocalDataRepository;
        mExecutors = executors;

        mRequests = new HashMap<>();

        mAppNameLocalDataRepository.getData()
                .observeForever(integerCursorEntry ->
                        onDataFetched(integerCursorEntry.getKey(), integerCursorEntry.getValue()));
    }

    public synchronized static AppNameRepository getInstance(
            AppNameNetworkDataSource AppNameNetworkDataSource,
            AppNameLocalDataRepository appNameLocalDataRepository,
            AppExecutors executors) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new AppNameRepository(AppNameNetworkDataSource,
                        appNameLocalDataRepository,
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

    public void requestData(OnDataRequestCallback callback, String filter) {
        int requestId = incrementRequestCounterAndGetNewValue();
        mRequests.put(requestId, callback);
        mAppNameLocalDataRepository.onSearchRequest(requestId, filter);
    }

    public void onDataFetched(int requestId, Cursor cursor) {
        OnDataRequestCallback callback = mRequests.get(requestId);
        callback.onDataFetched(cursor);
        mRequests.remove(requestId);
    }

    public void onDataLoadingFinished() {
        mAppNameLocalDataRepository.deleteLoading();
    }

    private synchronized int incrementRequestCounterAndGetNewValue() {
        requestCounter++;
        return requestCounter;
    }

    public interface OnDataRequestCallback {
        void onDataFetched(Cursor cursor);
    }

}
