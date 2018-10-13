package com.locationfencer.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "bookmark")
public class BookMark {
    @NonNull
    @PrimaryKey
    private String bookMarkId;

    @ColumnInfo(name = "placeName")
    private String placeName;

    @ColumnInfo(name = "latitude")
    private double latitude;

    @ColumnInfo(name = "longitude")
    private double longitude;

    public BookMark(@NonNull String bookMarkId, String placeName, double latitude, double longitude) {
        this.bookMarkId = bookMarkId;
        this.placeName = placeName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @NonNull
    public String getBookMarkId() {
        return bookMarkId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
