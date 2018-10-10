package com.locationfencer.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity(tableName = "locations")
public class Location {
    @NonNull @PrimaryKey
    private String requestId;

    @ColumnInfo(name = "placeName")
    private String placeName;

    @ColumnInfo(name = "reminder")
    private String reminder;

    public Location(String requestId, String placeName, String reminder) {
        this.requestId = requestId;
        this.placeName = placeName;
        this.reminder = reminder;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getReminder() {
        return reminder;
    }

}