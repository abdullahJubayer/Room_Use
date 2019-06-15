package com.aj.smartreminder.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.aj.smartreminder.Dao.LocationDao;
import com.aj.smartreminder.Model.LocationDataModel;

@Database(entities = {LocationDataModel.class},version = 3)
public abstract class MyDatabase extends RoomDatabase {

    private static MyDatabase DatabaseRef;
    public abstract LocationDao locationDao();

    public static synchronized MyDatabase databaseRef(Context context){
        if (DatabaseRef ==null){
            DatabaseRef= Room.databaseBuilder(context.getApplicationContext(),MyDatabase.class,
                    "LocationDatabase")
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return DatabaseRef;
    }

}
