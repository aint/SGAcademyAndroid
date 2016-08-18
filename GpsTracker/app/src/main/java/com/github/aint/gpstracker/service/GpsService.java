package com.github.aint.gpstracker.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.github.aint.gpstracker.activity.MainActivity.NEW_LOCATION_ACTION;

public class GpsService extends Service implements LocationListener {

    private static final String PERMISSION_NOT_GRANTED_TOAST = "Permission to location isn't granted";

    public GpsService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
            Toast.makeText(this, PERMISSION_NOT_GRANTED_TOAST, Toast.LENGTH_LONG).show();
            return 0;
        }

//        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onLocationChanged(Location location) {
        Intent intent = new Intent(NEW_LOCATION_ACTION);
        intent.putExtra("lat", location.getLatitude());
        intent.putExtra("lng", location.getLongitude());
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

}
