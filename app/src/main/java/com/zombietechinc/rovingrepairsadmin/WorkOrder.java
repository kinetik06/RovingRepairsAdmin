package com.zombietechinc.rovingrepairsadmin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 10/16/2017.
 */

public class WorkOrder {
    Vehicle vehicle;
    User user;

    String vehicleName;
    String customerName;
    String customerNumber;
    String customerAddress;
    String notes;
    double totalPrice;
    double laborPrice;
    double partsPrice;
    ArrayList<Job> jobList;
    ArrayList<Part> partList;


}
