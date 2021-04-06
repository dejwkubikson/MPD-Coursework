package com.example.mpd_coursework;

import android.os.Parcel;
import android.os.Parcelable;

// Dawid Kubiak (S1717751)
// EarthQuake class stores data for a single earthquake.
public class EarthQuake implements Parcelable {
    public int earthQuakeID = 0;
    public String location = "";
    public String eDate = "";
    public float latitude = 0.0f;
    public float longitude = 0.0f;
    public float magnitude = 0.0f;
    public int depth = 0;

    public EarthQuake()
    {
        // Empty public constructor
    }

    protected EarthQuake(Parcel in) {
        earthQuakeID = in.readInt();
        location = in.readString();
        eDate = in.readString();
        latitude = in.readFloat();
        longitude = in.readFloat();
        magnitude = in.readFloat();
        depth = in.readInt();
    }

    public static final Creator<EarthQuake> CREATOR = new Creator<EarthQuake>() {
        @Override
        public EarthQuake createFromParcel(Parcel in) {
            return new EarthQuake(in);
        }

        @Override
        public EarthQuake[] newArray(int size) {
            return new EarthQuake[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(earthQuakeID);
        parcel.writeString(location);
        parcel.writeString(eDate);
        parcel.writeFloat(latitude);
        parcel.writeFloat(longitude);
        parcel.writeFloat(magnitude);
        parcel.writeInt(depth);
    }
}
