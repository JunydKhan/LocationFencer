package com.locationfencer.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

@Entity(tableName = "locations")
public class Location implements Parcelable{

    @NonNull
    @PrimaryKey
    private String requestId;

    @ColumnInfo(name = "placeName")
    private String placeName;

    @ColumnInfo(name = "reminder")
    private String reminder;

    @ColumnInfo(name = "latitude")
    private Double latitude;

    @ColumnInfo(name = "longitude")
    private Double longitude;

    @ColumnInfo(name = "radius")
    private int radius;


    public Location(@NonNull String requestId, String placeName, String reminder, Double latitude, Double longitude, int radius) {
        this.requestId = requestId;
        this.placeName = placeName;
        this.reminder = reminder;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
    }

    protected Location(Parcel in) {
        requestId = in.readString();
        placeName = in.readString();
        reminder = in.readString();
        if (in.readByte() == 0) {
            latitude = null;
        } else {
            latitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            longitude = null;
        } else {
            longitude = in.readDouble();
        }
        radius = in.readInt();
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    public String getRequestId() {
        return requestId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getReminder() {
        return reminder;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public int getRadius() {
        return radius;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(requestId);
        dest.writeString(placeName);
        dest.writeString(reminder);
        if (latitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(latitude);
        }
        if (longitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(longitude);
        }
        dest.writeInt(radius);
    }
}