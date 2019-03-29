package msk.android.academy.javatemplate;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity/* implements MapView.MapControllerListener*/ {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        inflateMapFragment();
    }

    private void inflateMapFragment() {
        msk.android.academy.javatemplate.map.MapFragment mapFragment =
                msk.android.academy.javatemplate.map.MapFragment.getNewInstance();

        FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.doron_fragment, mapFragment);
            transaction.commit();
    }

}
