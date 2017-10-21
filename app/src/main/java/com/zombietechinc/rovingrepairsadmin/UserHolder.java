package com.zombietechinc.rovingrepairsadmin;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by User on 10/2/2017.
 */

public class UserHolder extends RecyclerView.ViewHolder {

    TextView userNameTV;
    TextView addressTV;
    TextView vinTV;
    TextView userNumberTV;
    View mView;


    public UserHolder(View itemView) {
        super(itemView);
        mView = itemView;
        userNameTV = (TextView)itemView.findViewById(R.id.usernameTV);
        addressTV = (TextView)itemView.findViewById(R.id.useraddressTV);
        userNumberTV = (TextView) itemView.findViewById(R.id.usernumberTV);
        //vinTV =(TextView)itemView.findViewById(R.id.vin_tv);
    }

}