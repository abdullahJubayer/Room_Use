package com.aj.smartreminder.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.aj.smartreminder.Model.LocationDataModel;

import java.util.List;

@Dao
public interface LocationDao {

    @Insert
    void insert(LocationDataModel model);

    @Update
    void update(LocationDataModel model);

    @Delete
    void delete(LocationDataModel model);

    @Query("SELECT * From `Location Table`")
    LiveData<List<LocationDataModel>> getAllData();

}
