package msk.android.academy.javatemplate.ui.utilities;

import android.content.Context;

import msk.android.academy.javatemplate.AppExecutors;
import msk.android.academy.javatemplate.data.AppNameRepository;
import msk.android.academy.javatemplate.data.network.AppNameNetworkDataSource;
import msk.android.academy.javatemplate.ui.user.UserInfoViewModelFactory;

public class InjectorUtils {

    public static AppNameRepository provideRepository(Context context) {
        AppExecutors executors = AppExecutors.getInstance();
        AppNameNetworkDataSource networkDataSource =
                AppNameNetworkDataSource.getInstance(context.getApplicationContext(), executors);
        return AppNameRepository.getInstance(networkDataSource, executors);
    }

    public static AppNameNetworkDataSource provideNetworkDataSource(Context context) {
        AppExecutors executors = AppExecutors.getInstance();
        return AppNameNetworkDataSource.getInstance(context.getApplicationContext(), executors);
    }

    public static UserInfoViewModelFactory provideUserInfoViewModelFactory(Context context) {
        AppNameRepository repository = provideRepository(context.getApplicationContext());
        return new UserInfoViewModelFactory(repository);
    }
}
