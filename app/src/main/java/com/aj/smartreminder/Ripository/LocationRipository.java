package com.aj.smartreminder.Ripository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.aj.smartreminder.Dao.LocationDao;
import com.aj.smartreminder.Database.MyDatabase;
import com.aj.smartreminder.Model.LocationDataModel;

import java.util.List;

public class LocationRipository {

    private LocationDao locationDao;
    private LiveData<List<LocationDataModel>> liveData;

    public LocationRipository(Application application){
        MyDatabase database=MyDatabase.databaseRef(application);
        locationDao=database.locationDao();
        liveData=locationDao.getAllData();
    }

    public void insert(LocationDataModel model){
        new insertLocationAsynk(locationDao).execute(model);
    }
    public void update(LocationDataModel model){
        new updateLocationAsynk(locationDao).execute(model);
    }
    public void delete(LocationDataModel model){
        new deleteLocationAsynk(locationDao).execute(model);
    }

    public LiveData<List<LocationDataModel>> getAllData(){
        return liveData;
    }

    private static class insertLocationAsynk extends AsyncTask<LocationDataModel,Void,Void>{

        private LocationDao locationDao;
        public insertLocationAsynk(LocationDao locationDao){
            this.locationDao=locationDao;
        }

        @Override
        protected Void doInBackground(LocationDataModel... locationDataModels) {

            locationDao.insert(locationDataModels[0]);
            return null;
        }
    }

    private static class updateLocationAsynk extends AsyncTask<LocationDataModel,Void,Void>{

        private LocationDao locationDao;
        public updateLocationAsynk(LocationDao locationDao){
            this.locationDao=locationDao;
        }

        @Override
        protected Void doInBackground(LocationDataModel... locationDataModels) {

            locationDao.update(locationDataModels[0]);
            return null;
        }
    }

    private static class deleteLocationAsynk extends AsyncTask<LocationDataModel,Void,Void>{

        private LocationDao locationDao;
        public deleteLocationAsynk(LocationDao locationDao){
            this.locationDao=locationDao;
        }

        @Override
        protected Void doInBackground(LocationDataModel... locationDataModels) {

            locationDao.delete(locationDataModels[0]);
            return null;
        }
    }


}
