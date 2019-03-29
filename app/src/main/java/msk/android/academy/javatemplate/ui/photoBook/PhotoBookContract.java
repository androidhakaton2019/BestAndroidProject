package msk.android.academy.javatemplate.ui.photoBook;

import java.util.List;

import msk.android.academy.javatemplate.data.AppNameRepository;

public interface PhotoBookContract {

    interface View {
        void onImagePathsFetched(List<String> imagePath);
    }

    interface Presenter extends AppNameRepository.OnDataRequestCallback {
        void onDataRequested(String filter);
    }
}
