package msk.android.academy.javatemplate.data.local;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import java.util.Map;

import msk.android.academy.javatemplate.AppExecutors;

/**
 * Created by rshlosberg.
 */

public class AppNameLocalDataRepository implements Loader.OnLoadCompleteListener<Cursor> {

    public static final Uri BASE_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static AppNameLocalDataRepository sInstance;
    private final Context mContext;

    private MutableLiveData<Map.Entry<Integer, Cursor>> data;

    private CursorLoader mCursorLoader;

    private final AppExecutors mExecutors;

    private AppNameLocalDataRepository(Context context, AppExecutors executors) {
        mContext = context;
        mExecutors = executors;

        data = new MutableLiveData<>();
    }

    /**
     * Get the singleton for this class
     */
    public static AppNameLocalDataRepository getInstance(Context context, AppExecutors executors) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new AppNameLocalDataRepository(context.getApplicationContext(), executors);
            }
        }
        return sInstance;
    }

    // These are the Contacts rows that we will retrieve.
    private static final String[] IMAGES_PROJECTION = new String[] {
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.DISPLAY_NAME
    };

    public void onSearchRequest(int requestId, String filter) {

        // Create a CursorLoader that will take care of creating a Cursor for the data being
        // displayed.
        String selection = null;
        String selectionArgs[] = null;
        if (filter != null) {
            selection = MediaStore.Images.Media.DATA + " LIKE ?";
            selectionArgs = new String[] {"%" + filter + "%"};
        }
        String sortOrder = MediaStore.Images.Media.DATA + " DESC";

        mCursorLoader = new CursorLoader(mContext, BASE_URI, IMAGES_PROJECTION, selection, selectionArgs, sortOrder);
        mCursorLoader.registerListener(requestId, this);
        mCursorLoader.startLoading();
    }

    @Override
    public void onLoadComplete(Loader<Cursor> loader, Cursor data) {
        this.data.postValue(new Map.Entry<Integer, Cursor>() {
            @Override
            public Integer getKey() {
                return loader.getId();
            }

            @Override
            public Cursor getValue() {
                return data;
            }

            @Override
            public Cursor setValue(Cursor value) {
                return value;
            }
        });
    }

    public void deleteLoading() {
        // Stop the cursor loader
        if (mCursorLoader != null) {
            mCursorLoader.unregisterListener(this);
            mCursorLoader.cancelLoad();
            mCursorLoader.stopLoading();
        }
    }

    public LiveData<Map.Entry<Integer, Cursor>> getData() {
        return data;
    }
}