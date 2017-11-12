package com.zombietechinc.rovingrepairsadmin;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by marcus on 10/21/17.
 */

public class Part implements Parcelable{
    String name;
    double price;

    public Part(){}

    private Part(Parcel in){

        name = in.readString();
        price = in.readDouble();
    }

    public Part(String name, double price){
        this.name = name;
        this.price = price;
    }

    public static final Creator<Part> CREATOR = new Creator<Part>() {
        @Override
        public Part createFromParcel(Parcel in) {
            return new Part(in);
        }

        @Override
        public Part[] newArray(int size) {
            return new Part[size];
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
