package com.zombietechinc.rovingrepairsadmin;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by marcus on 10/21/17.
 */

public class Job implements Parcelable{
    String name;
    double price;

    public Job(){}

    private Job (Parcel in){

        name = in.readString();
        price = in.readDouble();
    }

    public Job(String name, double price){
        this.name = name;
        this.price = price;
    }

    public static final Creator<Job> CREATOR = new Creator<Job>() {
        @Override
        public Job createFromParcel(Parcel in) {
            return new Job(in);
        }

        @Override
        public Job[] newArray(int size) {
            return new Job[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeDouble(price);
    }
}
