package msk.android.academy.javatemplate.ui.user;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import msk.android.academy.javatemplate.data.AppNameRepository;

/**
 * Factory method that allows us to create a ViewModel with a constructor that takes a
 */
public class UserInfoViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppNameRepository mRepository;

    public UserInfoViewModelFactory(AppNameRepository repository) {
        this.mRepository = repository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new UserInfoViewModel(mRepository);
    }
}
