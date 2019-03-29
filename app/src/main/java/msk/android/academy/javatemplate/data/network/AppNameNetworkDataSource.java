package msk.android.academy.javatemplate.data.network;

import android.content.Context;
import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import msk.android.academy.javatemplate.AppExecutors;
import msk.android.academy.javatemplate.ui.user.User;

public class AppNameNetworkDataSource {

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static AppNameNetworkDataSource sInstance;
    private final Context mContext;

    private final MutableLiveData<Uri> mUserPhotoUri;
    private final MutableLiveData<String> mUserName;
    private final MutableLiveData<String> mUserEmail;

    private final AppExecutors mExecutors;

    private AppNameNetworkDataSource(Context context, AppExecutors executors) {
        mContext = context;
        mExecutors = executors;

        mUserPhotoUri = new MutableLiveData<>();
        mUserName = new MutableLiveData<>();
        mUserEmail = new MutableLiveData<>();
    }

    /**
     * Get the singleton for this class
     */
    public static AppNameNetworkDataSource getInstance(Context context, AppExecutors executors) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new AppNameNetworkDataSource(context.getApplicationContext(), executors);
            }
        }
        return sInstance;
    }

    public void setFirebaseUser(FirebaseUser firebaseUser) {
        if (firebaseUser != null) {
            mUserPhotoUri.setValue(firebaseUser.getPhotoUrl());
            mUserName.setValue(firebaseUser.getDisplayName());
            mUserEmail.setValue(firebaseUser.getEmail());

            mExecutors.networkIO().execute(() -> {
                try {
                    User user = new User();
                    user.setUserId(firebaseUser.getUid());
                    user.setName(firebaseUser.getDisplayName());
                    user.setUserStatus(User.Status.USER);

//                    RegisterToServer registerToServer = new RegisterToServer(user);
//                    registerToServer.sendUser();
                } catch (Exception e) {
                    // Server probably invalid
                    e.printStackTrace();
                }
            });
        }
        else {
            mUserPhotoUri.setValue(null);
            mUserName.setValue(null);
            mUserEmail.setValue(null);
        }
    }

    public LiveData<Uri> getUserPhotoUri() {
        return mUserPhotoUri;
    }

    public LiveData<String> getUserName() {
        return mUserName;
    }

    public LiveData<String> getUserEmail() {
        return mUserEmail;
    }

}
