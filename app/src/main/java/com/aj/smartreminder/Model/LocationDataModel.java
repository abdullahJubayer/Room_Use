package com.aj.smartreminder.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Location Table")
public class LocationDataModel implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private String roadNo;
    private Double latitude;
    private Double longitude;
    private String priority;

    public LocationDataModel(String title, String description, String roadNo, Double latitude, Double longitude, String priority) {
        this.title = title;
        this.description = description;
        this.roadNo = roadNo;
        this.latitude = latitude;
        this.longitude = longitude;
        this.priority = priority;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getRoadNo() {
        return roadNo;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getPriority() {
        return priority;
    }
}
