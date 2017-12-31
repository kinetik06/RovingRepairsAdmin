package com.zombietechinc.rovingrepairsadmin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 10/16/2017.
 */

public class WorkOrder {
    public Vehicle vehicle;
    public User user;
    public String notes;
    public double totalPrice;
    public double laborPrice;
    public double partsPrice;
    public ArrayList<Job> jobList;
    public ArrayList<Part> partList;

    public WorkOrder(){}

    public WorkOrder(User user, Vehicle vehicle, String notes, ArrayList<Job> jobList, ArrayList<Part> partList,
                      double partsPrice, double laborPrice, double totalPrice ) {
        this.user = user;
        this.vehicle = vehicle;
        this.notes = notes;
        this.jobList = jobList;
        this.partList = partList;
        this.partsPrice = partsPrice;
        this.laborPrice = laborPrice;
        this.totalPrice = totalPrice;
    }


}
