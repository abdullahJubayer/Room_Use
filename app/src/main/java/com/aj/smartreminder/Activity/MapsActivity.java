package com.aj.smartreminder.Activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.aj.smartreminder.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    FusedLocationProviderClient fusedLocationProviderClient;
    private boolean isLocationPermissionGranted = false;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_id);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        } else {
            isLocationPermissionGranted = true;
            getCurrentLocation();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            isLocationPermissionGranted = true;
            getCurrentLocation();
        } else {
            Toast.makeText(MapsActivity.this, "Location Permission Needed", Toast.LENGTH_SHORT).show();
        }
    }

    private void getCurrentLocation() {
        if (isLocationPermissionGranted) {

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        Toast.makeText(MapsActivity.this, "Location", Toast.LENGTH_SHORT).show();
                        LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(latLng).title("My Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,17f));
                        CreateNewTask.location_et.setText("My Location");
                        CreateNewTask.latitude.setText(String.valueOf(latLng.latitude));
                        CreateNewTask.longitude.setText(String.valueOf(latLng.longitude));

                    } else {
                        Toast.makeText(MapsActivity.this, "Location Null", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            Toast.makeText(this, "Permission Not Granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        Geocoder geocoder=new Geocoder(getApplicationContext());
        try {
            ArrayList<Address> addresses = (ArrayList<Address>) geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
            String title;
                title=addresses.get(0).getAddressLine(0);
            if (title ==null){
                title="Location";
            }
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(latLng).title(title).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            CreateNewTask.location_et.setText(title);
            CreateNewTask.latitude.setText(String.valueOf(latLng.latitude));
            CreateNewTask.longitude.setText(String.valueOf(latLng.longitude));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
