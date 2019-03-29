package msk.android.academy.javatemplate.ui.user;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import msk.android.academy.javatemplate.ui.utilities.InjectorUtils;

public class LogoutFragment extends Fragment {

    private UserInfoViewModel mViewModel;

    public static LogoutFragment getNewInstance() {
        LogoutFragment fragment = new LogoutFragment();
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

        performLogout();
    }

    public void performLogout() {
//        Context context = getContext();
//        if (context != null) {
//            Context applicationContext = getContext().getApplicationContext();
            FirebaseAuth.getInstance()
                    .signOut();
//                    .signOut(applicationContext)
//                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                        public void onComplete(@NonNull Task<Void> task) {
//                            mViewModel.setFirebaseUser(null);
//                        }
//                    });
//        }
    }
}
