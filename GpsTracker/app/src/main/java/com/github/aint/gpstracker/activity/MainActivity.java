package com.github.aint.gpstracker.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.support.v7.app.AppCompatActivity;

import com.github.aint.gpstracker.R;
import com.github.aint.gpstracker.service.GpsService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, OnRequestPermissionsResultCallback {

    public static final String NEW_LOCATION_ACTION = "new_location";

    private final static int REQUEST_LOCATION = 121;

    private GoogleMap mMap;
    private Marker currentLocationMarker;
    private Marker onClickMarker;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (NEW_LOCATION_ACTION.equals(intent.getAction())) {
                LatLng latLng = new LatLng(intent.getDoubleExtra("lat", 0), intent.getDoubleExtra("lng", 0));
                currentLocationMarker.setPosition(latLng);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment =  (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        startService(new Intent(this, GpsService.class));

        registerReceiver(broadcastReceiver, new IntentFilter(NEW_LOCATION_ACTION));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                if (onClickMarker == null) {
                    onClickMarker = mMap.addMarker(new MarkerOptions().position(point));
                }
                onClickMarker.setPosition(point);
            }
        });
        mMap.getUiSettings().setZoomControlsEnabled(true);

        LatLng rivne = new LatLng(50.61, 26.24);
        currentLocationMarker = mMap.addMarker(new MarkerOptions().position(rivne).title("Marker in Rivne"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(rivne, 10));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(rivne));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);



    }
}
