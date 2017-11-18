package com.zombietechinc.rovingrepairsadmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ConfirmWorkOrderActivity extends AppCompatActivity {

    ArrayList<Job> jobList;
    ArrayList<Part> partList;
    public FirebaseDatabase mFirebaseDatabase;
    public DatabaseReference mDatabaseReference;
    public DatabaseReference vehicleRef;
    String userID;
    String vehicleKey;
    TextView nameTV;
    TextView addressTV;
    TextView numberTV;
    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    private LinearLayoutManager manager;
    RecyclerView partRecyclerView;
    RecyclerView.Adapter partAdapter;
    private LinearLayoutManager partManager;
    double jobTotal = 0;
    TextView totalLaborTV;
    double partTotal = 0;
    TextView totalPartsTV;
    TextView totalPriceTV;
    double totalPrice = 0;
    String ymm = "";
    TextView vehicleNameTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_work_order);
        totalPriceTV = findViewById(R.id.totalPriceTV);
        nameTV = findViewById(R.id.nameTV);
        addressTV = findViewById(R.id.addressTV);
        numberTV =findViewById(R.id.numberTV);
        vehicleNameTV = findViewById(R.id.vehicleNameTV);
        totalLaborTV = findViewById(R.id.totalLaborTV);
        totalPartsTV = findViewById(R.id.totalPartsTV);
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        vehicleKey = intent.getStringExtra("vehicleKey");
        partList = getIntent().getParcelableArrayListExtra("partlist");
        jobList = getIntent().getParcelableArrayListExtra("joblist");
        Log.d("Part List Size: ", String.valueOf(partList.size()));


        recyclerView = findViewById(R.id.second_job_rv);
        mAdapter = new JobAdapter(ConfirmWorkOrderActivity.this, jobList);
        manager = new LinearLayoutManager(ConfirmWorkOrderActivity.this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);
        partManager = new LinearLayoutManager(ConfirmWorkOrderActivity.this);
        partRecyclerView = findViewById(R.id.part_rv);
        partAdapter = new PartAdapter(ConfirmWorkOrderActivity.this, partList);
        partRecyclerView.setLayoutManager(partManager);
        partRecyclerView.setAdapter(partAdapter);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("users/" + userID);
        vehicleRef = mFirebaseDatabase.getReference("vehicles/" + userID + "/" + vehicleKey);
        Log.d("User: ", userID);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    String fullName = user.getName() + " " + user.getLastName();
                    nameTV.setText(fullName);
                    addressTV.setText(user.getAddress());
                    numberTV.setText(user.getContactnumber());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mDatabaseReference.addValueEventListener(valueEventListener);

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Vehicle vehicle = dataSnapshot.getValue(Vehicle.class);
                ymm = vehicle.getYear() + " " + vehicle.getMake() + " " + vehicle.getModel();
                vehicleNameTV.setText(ymm);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        vehicleRef.addValueEventListener(eventListener);

        for (int i = 0; i < jobList.size(); i++) {
            Job job = jobList.get(i);
            jobTotal = jobTotal + job.getPrice();
            Log.d("Job Price: ", String.valueOf(jobTotal));

        }
        totalLaborTV.setText("Labor Total: $" + String.valueOf(jobTotal) + 0);

        for (int i = 0; i < partList.size(); i++) {
            Part part = partList.get(i);
            partTotal = partTotal + part.getPrice();
            Log.d("Job Price: ", String.valueOf(partTotal));

        }
        totalPartsTV.setText("Parts Total: $" + String.valueOf(partTotal) + 0);

        totalPrice = jobTotal + partTotal;
        totalPriceTV.setText("Total: $" + String.valueOf(totalPrice) + 0);
    }
}
