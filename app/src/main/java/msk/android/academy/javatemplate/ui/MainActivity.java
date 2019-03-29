package msk.android.academy.javatemplate.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import msk.android.academy.javatemplate.R;
import msk.android.academy.javatemplate.ui.photoBook.PhotoBookFragment;
import msk.android.academy.javatemplate.ui.user.LoginFragment;
import msk.android.academy.javatemplate.ui.user.LogoutFragment;
import msk.android.academy.javatemplate.ui.user.UserInfoViewModel;
import msk.android.academy.javatemplate.ui.user.UserInfoViewModelFactory;
import msk.android.academy.javatemplate.ui.utilities.InjectorUtils;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final String FRAGMENT_TAG = "fragment";

    private DrawerLayout mDrawerLayout;

    private NavigationView mNavigationView;
    private View mHeaderLayout;

    private UserInfoViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbar();

        // Get the ViewModel from the factory
        UserInfoViewModelFactory factory = InjectorUtils.provideUserInfoViewModelFactory(getApplicationContext());
        mViewModel = ViewModelProviders.of(this, factory).get(UserInfoViewModel.class);

        // Observers changes in the Firebase user
        mViewModel.getUserPhotoUri().observe(this, userPhotoUri -> {
            // If the user photo uri details change, update the UI
            if (userPhotoUri != null) bindUserPhotoUriToUI(userPhotoUri);
        });
        mViewModel.getUserName().observe(this, userName -> {
            // If the user name details change, update the UI
            if (userName != null) bindUserNameToUI(userName);
        });
        mViewModel.getUserEmail().observe(this, userEmail -> {
            // If the user email details change, update the UI
            if (userEmail != null) bindUserEmailToUI(userEmail);
        });

        setupNavigationDrawer();

        inflatePhotoBookFragment();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void performLogin() {
        // Create the fragment
        LoginFragment loginFragment = LoginFragment.getNewInstance();

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (! fragmentManager.isStateSaved()) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment, loginFragment, FRAGMENT_TAG);
            transaction.commit();
        }
    }

    private void setupNavigationDrawer() {
        if (mDrawerLayout == null)
            mDrawerLayout = findViewById(R.id.drawer_layout);

        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        mHeaderLayout = mNavigationView.getHeaderView(0); // 0-index header

        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionbar.setDisplayShowTitleEnabled(false);
        }
    }

    private void performLogout() {
        // Create the fragment
        LogoutFragment logoutFragment = LogoutFragment.getNewInstance();

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (! fragmentManager.isStateSaved()) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment, logoutFragment, FRAGMENT_TAG);
            transaction.commit();
        }
    }

    @Override
    public void onBackPressed() {

        if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle navigation view item clicks

        // Close drawer when item is tapped
        mDrawerLayout.closeDrawers();

        switch (menuItem.getItemId()) {
            case R.id.nav_action_logout:
                // Set item as not checkable to not persist highlight
                menuItem.setCheckable(false);
                performLogout();
                performLogin();
                inflatePhotoBookFragment();
                return true;
        }

        return true;
    }

    private void bindUserPhotoUriToUI(Uri photoUri) {
        ImageView ivNavHeaderUserPhoto = mHeaderLayout.findViewById(R.id.iv_nav_header_image_view);
        Glide.with(this).load(photoUri).into(ivNavHeaderUserPhoto);
    }

    private void bindUserNameToUI(String userName) {
        TextView tvNavHeaderUserName = mHeaderLayout.findViewById(R.id.tv_nav_header_user_name);
        tvNavHeaderUserName.setText(userName);
    }

    private void bindUserEmailToUI(String userEmail) {
        TextView tvNavHeaderUserEmail = mHeaderLayout.findViewById(R.id.nav_header_user_email);
        tvNavHeaderUserEmail.setText(userEmail);
    }

    private void inflatePhotoBookFragment() {
        // Create the fragment
        PhotoBookFragment photoBookFragment = PhotoBookFragment.getNewInstance();

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (! fragmentManager.isStateSaved()) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment, photoBookFragment, FRAGMENT_TAG);
            transaction.commit();
        }
    }
}

