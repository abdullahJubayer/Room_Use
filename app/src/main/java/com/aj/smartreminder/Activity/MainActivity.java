package com.aj.smartreminder.Activity;

import android.Manifest;
import android.app.PendingIntent;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.aj.smartreminder.Adapter.MyRecyclerAdapter;
import com.aj.smartreminder.Model.LocationDataModel;
import com.aj.smartreminder.Model.LocationViewModel;
import com.aj.smartreminder.Model.RecyclerViewholder;
import com.aj.smartreminder.R;
import com.aj.smartreminder.Services.LocationSer2;
import com.aj.smartreminder.Services.LocationService;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, RecyclerViewholder.MyRecyclerItemClick {

    private static final String Key = "com.aj.smartreminder." + "NewTaskData";
    private FloatingActionButton floatingActionButton;
    private LocationViewModel locationViewModel;
    private RecyclerView recyclerView;
    private MyRecyclerAdapter myAdapter;
    List<LocationDataModel> locationData;
    private GeofencingClient geofencingClient;
    private PendingIntent pendingIntent;
    private List<Geofence> geoFanceList=new ArrayList<>();
    private boolean isLocationPermissionGranted = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Today Task");

        geofencingClient = LocationServices.getGeofencingClient(this);
        recyclerView = findViewById(R.id.recycler_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        myAdapter = new MyRecyclerAdapter(this, this);
        recyclerView.setAdapter(myAdapter);

        floatingActionButton = findViewById(R.id.floating_action_id);
        floatingActionButton.setOnClickListener(this);

        locationViewModel = ViewModelProviders.of(this).get(LocationViewModel.class);
        locationViewModel.getAllData().observe(this, new Observer<List<LocationDataModel>>() {
            @Override
            public void onChanged(@Nullable List<LocationDataModel> locationDataModels) {
                myAdapter.setData(locationDataModels);
                locationData = locationDataModels;

                if (locationDataModels.size() == 0) {
                    Toast.makeText(MainActivity.this, "No Task Added", Toast.LENGTH_SHORT).show();
                }
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                LocationDataModel data = myAdapter.getSingleData(viewHolder.getAdapterPosition());
                locationViewModel.delete(data);
                Toast.makeText(MainActivity.this, "Delete", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);
    }


    @Override
    public void onClick(View v) {
        if (v == floatingActionButton) {
            openNewActivity();
        }
    }

    private void openNewActivity() {
        startActivityForResult(new Intent(MainActivity.this, CreateNewTask.class), 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == RESULT_OK) {
            LocationDataModel Data = (LocationDataModel) data.getSerializableExtra(Key);
            locationViewModel.insert(Data);
            Toast.makeText(this, "Data Save", Toast.LENGTH_SHORT).show();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                isLocationPermissionGranted = true;
                addGeofances(Data);
            }

        }else {
            Toast.makeText(this, "Failed to Save Data", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            isLocationPermissionGranted = true;
        } else {
            Toast.makeText(MainActivity.this, "Location Permission Needed", Toast.LENGTH_SHORT).show();
        }
    }

    private void addGeofances(LocationDataModel data) {
        if (isLocationPermissionGranted){

            Geofence geofence=new Geofence.Builder()
                    .setRequestId(data.getTitle())
                    .setCircularRegion(
                            data.getLatitude(),
                            data.getLongitude(),
                            200
                    )
                    .setExpirationDuration(6 * 60 * 60 * 1000)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build();
            geoFanceList.add(geofence);

            Log.e("Latlng",data.getLatitude()+" "+data.getLongitude()+" "+data.getTitle());

                geofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                        .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "Geofenc Added ", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .addOnFailureListener(this, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Geofenc Added Failed", Toast.LENGTH_SHORT).show();

                            }
                        });
            }
        else {
            Toast.makeText(this, "Permission Not Granted", Toast.LENGTH_SHORT).show();
        }
    }


    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(geoFanceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        if (pendingIntent != null) {
            return pendingIntent;
        }
        Intent intent = new Intent(this, LocationSer2.class);
        pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    @Override
    public void Click(int position) {
        startActivity(new Intent(MainActivity.this,EditActivity.class).putExtra("location",locationData.get(position)));
    }

}
