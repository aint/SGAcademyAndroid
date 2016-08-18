package com.github.aint.gpstracker.service;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.github.aint.gpstracker.activity.MainActivity.NEW_LOCATION_ACTION;

public class LocationService extends Service implements LocationListener {

    public static final String LAT_ATTRIBUTE = "lat";
    public static final String LNG_ATTRIBUTE = "lng";

    private static final String PERMISSION_NOT_GRANTED_TOAST = "Permission to location isn't granted";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (checkLocationPermission()) {
            Toast.makeText(this, PERMISSION_NOT_GRANTED_TOAST, Toast.LENGTH_LONG).show();
            return 0;
        }

        ((LocationManager) getSystemService(LOCATION_SERVICE))
                .requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this);

        return super.onStartCommand(intent, flags, startId);
    }

    private boolean checkLocationPermission() {
        return ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED;
    }

    @Override
    public void onLocationChanged(Location location) {
        Intent intent = new Intent(NEW_LOCATION_ACTION);
        intent.putExtra(LAT_ATTRIBUTE, location.getLatitude());
        intent.putExtra(LNG_ATTRIBUTE, location.getLongitude());
        sendBroadcast(intent);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
