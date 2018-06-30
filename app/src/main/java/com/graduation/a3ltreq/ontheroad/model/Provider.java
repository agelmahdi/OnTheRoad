package com.graduation.a3ltreq.ontheroad.model;


import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ahmed El-Mahdi on 7/1/2017.
 */

public  class Provider implements Parcelable {
    private int p_id;

    private double lat;
    private double lng;


    protected Provider(Parcel in) {
        p_id = in.readInt();
        lat = in.readDouble();
        lng = in.readDouble();
        mName = in.readString();
        phone = in.readString();
        location = in.readString();
        s_id = in.readInt();
        s_name = in.readString();
    }

    public static final Creator<Provider> CREATOR = new Creator<Provider>() {
        @Override
        public Provider createFromParcel(Parcel in) {
            return new Provider(in);
        }

        @Override
        public Provider[] newArray(int size) {
            return new Provider[size];
        }
    };

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getmName() {
        return mName;
    }

    public int getP_id() {
        return p_id;
    }

    public String getPhone() {
        return phone;
    }

    public String getLocation() {
        return location;
    }

    public int getS_id() {
        return s_id;
    }

    public String getS_name() {
        return s_name;
    }

    private String mName;
    private String phone;
    private String location;
    private int s_id;
    private String s_name;

    public Provider(JSONObject Provider) throws JSONException {
        this.p_id = Provider.getInt("id");
        this.mName = Provider.getString("name");
        this.phone = Provider.getString("phone");
        this.location = Provider.getString("location");
        this.s_id = Provider.getInt("s_id");
        this.s_name = Provider.getString("s_name");
        this.lat = Provider.getDouble("lat");
        this.lng = Provider.getDouble("lng");

    }

    public Provider(int p_id, String Name, String phone, String location, int s_id, String s_name) {

        this.p_id = p_id;
        this.mName = Name;
        this.phone = phone;
        this.location = location;
        this.s_id = s_id;
        this.s_name = s_name;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(p_id);
        parcel.writeDouble(lat);
        parcel.writeDouble(lng);
        parcel.writeString(mName);
        parcel.writeString(phone);
        parcel.writeString(location);
        parcel.writeInt(s_id);
        parcel.writeString(s_name);
    }
}