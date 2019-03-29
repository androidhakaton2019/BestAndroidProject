package msk.android.academy.javatemplate;

// import android.support.v7.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcel;

import com.here.mobility.sdk.core.auth.UserAuthenticationException;
import com.here.mobility.sdk.core.geo.GeoPointsList;
import com.here.mobility.sdk.core.geo.LatLng;
import com.here.mobility.sdk.map.Anchor;
import com.here.mobility.sdk.map.AnnotationColor;
import com.here.mobility.sdk.map.CircleStyle;
import com.here.mobility.sdk.map.GeoShapeStyle;
import com.here.mobility.sdk.map.MapController;
import com.here.mobility.sdk.map.MapFragment;
import com.here.mobility.sdk.map.MapImageStyle;
import com.here.mobility.sdk.map.MapObjectStyle;
import com.here.mobility.sdk.map.MapView;
import com.here.mobility.sdk.map.Marker;
import com.here.mobility.sdk.map.PolylineStyle;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

// import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements MapView.MapControllerListener {

    private MapController mMapController;

    private static final int PERMISSIONS_REQUEST_CODE = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //MapFragment initialization.
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_fragment);
        if (mapFragment != null) {
            mapFragment.loadMapAsync(this);
        }
    }

    @Override
    @SuppressLint("MissingPermission")
    public void onMapReady(@NonNull MapController aMapController) {
        this.mMapController = aMapController;
        LocationManager mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Location lastKnownLocation = null;
        if (this.isPermissionGranted()) {
            lastKnownLocation = mLocationManager.getLastKnownLocation("fused");
        } else {
            this.requestPermission();
        }

        if (lastKnownLocation != null) {
            LatLng currentLocation = LatLng.fromLocation(lastKnownLocation);
            // LatLng currentLocation = LatLng.fromDegrees(51.497392, -0.115057);

            aMapController.setPosition(currentLocation);
            this.addMarker(currentLocation, true);

            //Adding mock locations
            List<LatLng> locationsList = this.getLocations(currentLocation);

            for (LatLng latLng : locationsList) {
                this.addMarker(latLng, false);
            }

            PolylineStyle.Builder builder = PolylineStyle.builder();
            builder.setCapStyle(PolylineStyle.CapStyle.ROUND);
            builder.setWidth(1.2f);
            builder.setColor(AnnotationColor.createFromRgba(0, 0, 0, 200));
            locationsList.add(0, currentLocation);
            this.mMapController.addPolyline(GeoPointsList.createNoCopy(locationsList), builder.build());

        }
        aMapController.setZoom(15.5f);
    }

    private List<LatLng> getLocations(LatLng aCurrentLocation) {
        List<LatLng> locationsList = new ArrayList<LatLng>();
        locationsList.add(LatLng.fromDegrees(aCurrentLocation.getLatDeg() + 0.001,
                aCurrentLocation.getLngDeg() + 0.001));
        locationsList.add(LatLng.fromDegrees(aCurrentLocation.getLatDeg() + 0.001,
                aCurrentLocation.getLngDeg() + 0.004));
        locationsList.add(LatLng.fromDegrees(aCurrentLocation.getLatDeg() + 0.002,
                aCurrentLocation.getLngDeg() + 0.001));
        locationsList.add(LatLng.fromDegrees(aCurrentLocation.getLatDeg() - 0.001,
                aCurrentLocation.getLngDeg() - 0.005));
        return locationsList;
    }

    @Override
    public void onMapInitializationFailure(@NonNull Exception e) {

    }

    @Override
    public void onAuthenticationFailure(@NonNull UserAuthenticationException e) {

    }

    private boolean isPermissionGranted() {
        return ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_FINE_LOCATION)) {
            showExplainingRationaleDialog(); // next slide
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_CODE);
        }

    }

    private void showExplainingRationaleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Dear User");
        builder.setMessage(
                "We need access to your storage in order to download the image file you requested");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestPermission();
            }
        });

        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishActivity(PERMISSIONS_REQUEST_CODE);
            }
        });

        builder.create().show();
    }

    @NonNull
    public Marker addMarker(LatLng aMarkerLocation, boolean aIsCurrent) {
        MapObjectStyle mapObjectStyle = null;
        if (aIsCurrent) {
            CircleStyle.Builder builder = CircleStyle.builder();
            builder.setColor(AnnotationColor.createFromRgba(250, 0, 0, 200));
            builder.setRadiusPixels(20);
            mapObjectStyle = builder.build();
        } else {
            MapImageStyle.Builder builder = MapImageStyle.builder(this.getBaseContext(), R.drawable.ic_place_black_24dp);
            builder.setClickable(true);
            builder.setSizePixels(30, 30);
            mapObjectStyle = builder.build();
        }

        Marker marker = this.mMapController.addMarker(aMarkerLocation, mapObjectStyle);

        return marker;
    }


}
