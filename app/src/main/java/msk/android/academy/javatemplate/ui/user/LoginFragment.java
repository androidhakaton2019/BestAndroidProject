package msk.android.academy.javatemplate.ui.user;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;
import java.util.List;

import msk.android.academy.javatemplate.R;
import msk.android.academy.javatemplate.ui.utilities.InjectorUtils;

import static android.app.Activity.RESULT_OK;

public class LoginFragment extends Fragment {

    private static final int RC_SIGN_IN = 123;

    private UserInfoViewModel mViewModel;

    public static LoginFragment getNewInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Activity activity = getActivity();
        if (activity == null) return;
        Context applicationContext = activity.getApplicationContext();

        // Get the ViewModel from the factory
        UserInfoViewModelFactory factory = InjectorUtils.provideUserInfoViewModelFactory(applicationContext);
        mViewModel = ViewModelProviders.of(this, factory).get(UserInfoViewModel.class);

        performLogin();
    }

    public void performLogin() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build()
//                , new AuthUI.IdpConfig.PhoneBuilder().build()
//                , new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        Intent intent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.mipmap.ic_launcher_round)
                .setTheme(R.style.AppTheme)
                .build();

        startActivityForResult(intent, RC_SIGN_IN);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                mViewModel.setFirebaseUser(user);
                // ...
            } else {
                // Sign in failed, check response for error code
                // ...
            }
        }
    }
}
