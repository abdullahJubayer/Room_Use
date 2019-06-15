package com.aj.smartreminder.Model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.aj.smartreminder.Ripository.LocationRipository;

import java.util.List;

public class LocationViewModel extends AndroidViewModel {

    private LocationRipository locationRipository;
    private LiveData<List<LocationDataModel>> allData;

    public LocationViewModel(@NonNull Application application) {
        super(application);

        locationRipository=new LocationRipository(application);
        allData=locationRipository.getAllData();
    }

    public void insert(LocationDataModel model){
        locationRipository.insert(model);
    }

    public void update(LocationDataModel model){
        locationRipository.update(model);
    }

    public void delete(LocationDataModel model){
        locationRipository.delete(model);
    }

    public LiveData<List<LocationDataModel>> getAllData(){
        return allData;
    }
}
