package com.zombietechinc.rovingrepairsadmin;

/**
 * Created by User on 10/22/2017.
 */





        import java.util.ArrayList;



        import android.os.Parcel;

        import android.os.Parcelable;



public class JobsList extends ArrayList<Job> implements Parcelable{



    private static final long serialVersionUID = 663585476779879096L;



    public JobsList(){



    }



    public JobsList(Parcel in){

        readFromParcel(in);

    }



    @SuppressWarnings("unchecked")

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        public JobsList createFromParcel(Parcel in) {

            return new JobsList(in);

        }



        public Object[] newArray(int arg0) {

            return null;

        }



    };



    private void readFromParcel(Parcel in) {

        this.clear();



        //First we have to read the list size

        int size = in.readInt();



        //Reading remember that we wrote first the Name and later the Phone Number.

        //Order is fundamental



        for (int i = 0; i < size; i++) {

            Job c = new Job();

            c.setName(in.readString());

            c.setPhoneNumber(in.readString());

            this.add(c);

        }



    }



    public int describeContents() {

        return 0;

    }



    public void writeToParcel(Parcel dest, int flags) {

        int size = this.size();

        //We have to write the list size, we need him recreating the list

        dest.writeInt(size);

        //We decided arbitrarily to write first the Name and later the Phone Number.

        for (int i = 0; i < size; i++) {

            Customer c = this.get(i);

            dest.writeString(c.getName());

            dest.writeString(c.getPhoneNumber());

        }

    }





}
