package com.zombietechinc.rovingrepairsadmin;

/**
 * Created by marcus on 10/21/17.
 */

public class Job {
    String name;
    double price;

    public Job(){}

    public Job(String name, double price){}

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
}