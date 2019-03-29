package msk.android.academy.javatemplate.ui.user;

import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import msk.android.academy.javatemplate.data.AppNameRepository;

public class UserInfoViewModel extends ViewModel {

    private final LiveData<Uri> mUserPhotoUri;
    private final LiveData<String> mUserName;
    private final LiveData<String> mUserEmail;

    private final AppNameRepository mRepository;

    public UserInfoViewModel(AppNameRepository repository) {
        mRepository = repository;
        mUserPhotoUri = mRepository.getUserPhotoUri();
        mUserName = mRepository.getUserName();
        mUserEmail = mRepository.getUserEmail();
    }

    public void setFirebaseUser(FirebaseUser firebaseUser) {
        mRepository.setFirebaseUser(firebaseUser);
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
