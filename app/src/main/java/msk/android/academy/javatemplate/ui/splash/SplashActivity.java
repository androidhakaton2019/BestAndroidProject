package msk.android.academy.javatemplate.ui.splash;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import msk.android.academy.javatemplate.ui.MainActivity;
import msk.android.academy.javatemplate.ui.user.LoginFragment;
import msk.android.academy.javatemplate.ui.user.UserInfoViewModel;
import msk.android.academy.javatemplate.ui.user.UserInfoViewModelFactory;
import msk.android.academy.javatemplate.ui.utilities.InjectorUtils;

public class SplashActivity extends AppCompatActivity {

    private final String FRAGMENT_TAG = "fragment";

    private UserInfoViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkFirstRun();

        // Get the ViewModel from the factory
        UserInfoViewModelFactory factory = InjectorUtils.provideUserInfoViewModelFactory(getApplicationContext());
        mViewModel = ViewModelProviders.of(this, factory).get(UserInfoViewModel.class);

        // Observers changes in the Firebase user
        mViewModel.getUserPhotoUri().observe(this, userPhotoUri -> {
            // If the user photo uri details change, update the UI
            if (userPhotoUri != null) startApp();
        });

        performLogin();

        //startApp();
    }

    private void checkFirstRun() {

    }

    private void performLogin() {
        // Create the fragment
        LoginFragment loginFragment = LoginFragment.getNewInstance();

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (!fragmentManager.isStateSaved()) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(loginFragment, FRAGMENT_TAG);
            transaction.commit();
        }
    }

    private void startApp() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}