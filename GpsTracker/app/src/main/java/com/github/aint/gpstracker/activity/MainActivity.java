package com.github.aint.gpstracker.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.github.aint.gpstracker.R;
import com.github.aint.gpstracker.service.LocationService;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.github.aint.gpstracker.service.LocationService.LAT_ATTRIBUTE;
import static com.github.aint.gpstracker.service.LocationService.LNG_ATTRIBUTE;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, OnMapClickListener,
        OnRequestPermissionsResultCallback, ConnectionCallbacks {

    public static final String NEW_LOCATION_ACTION = "new_location";

    private final static int REQUEST_LOCATION = 121;
    private static final String PERMISSION_NOT_GRANTED_TOAST = "Permission to location not granted";
    private static final String YOU_ARE_HERE_LABEL = "You are here";

    private GoogleApiClient googleApiClient;

    private GoogleMap mMap;
    private Marker currentLocationMarker;
    private Marker onClickMarker;

    private Location lastLocation;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (NEW_LOCATION_ACTION.equals(intent.getAction())) {
                LatLng latLng = new LatLng(intent.getDoubleExtra(LAT_ATTRIBUTE, 0), intent.getDoubleExtra(LNG_ATTRIBUTE, 0));
                currentLocationMarker.setPosition(latLng);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);

        getGoogleApiClient();

        initServiceAndReceiver();
    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    private void getGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void initServiceAndReceiver() {
        startService(new Intent(this, LocationService.class));
        registerReceiver(broadcastReceiver, new IntentFilter(NEW_LOCATION_ACTION));
    }

    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        setUpMap(googleMap);
        setCurrentPosition();
    }

    private void setUpMap(GoogleMap googleMap) {
        mMap = googleMap;
        checkLocationPermission();
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapClickListener(this);
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    private void setCurrentPosition() {
        LatLng location = getCurrentLocation();
        currentLocationMarker = mMap.addMarker(new MarkerOptions().position(location).title(YOU_ARE_HERE_LABEL));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
    }

    private LatLng getCurrentLocation() {
        if (lastLocation != null) {
            return new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        }
        return new LatLng(50.62, 26.25);
    }

    @Override
    public void onMapClick(LatLng point) {
        if (onClickMarker == null) {
            onClickMarker = mMap.addMarker(new MarkerOptions().position(point));
        }
        onClickMarker.setPosition(point);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if (!(grantResults.length == 1 && grantResults[0] == PERMISSION_GRANTED)) {
                Toast.makeText(this, PERMISSION_NOT_GRANTED_TOAST, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        checkLocationPermission();
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }
}
