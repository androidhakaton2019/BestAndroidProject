package msk.android.academy.javatemplate.ui.photoBook;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

import msk.android.academy.javatemplate.data.AppNameRepository;
import msk.android.academy.javatemplate.ui.utilities.InjectorUtils;

public class PhotoBookPresenter implements PhotoBookContract.Presenter {

    private PhotoBookContract.View mView;

    private AppNameRepository mDataRepository;

    public PhotoBookPresenter(PhotoBookContract.View view, Context context) {
        mView = view;
        mDataRepository = InjectorUtils.provideRepository(context);
    }

    @Override
    public void onDataRequested(String filter) {
        mDataRepository.requestData(this, filter);
    }

    @Override
    public void onDataFetched(Cursor cursor) {

        List<String> photoPaths = new ArrayList<>();

        // Using cursor data get all image details.
        while (cursor.moveToNext()) {
            String imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

            photoPaths.add(imagePath);
        }

        cursor.close();
        mDataRepository.onDataLoadingFinished();

        mView.onImagePathsFetched(photoPaths);
    }
}
