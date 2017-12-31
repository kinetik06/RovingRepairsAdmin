package com.zombietechinc.rovingrepairsadmin;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.sdk.pos.ChargeRequest;
import com.squareup.sdk.pos.CurrencyCode;
import com.squareup.sdk.pos.PosClient;
import com.squareup.sdk.pos.PosSdk;

import java.util.ArrayList;

import static com.squareup.sdk.pos.CurrencyCode.USD;

public class ConfirmWorkOrderActivity extends AppCompatActivity {

    ArrayList<Job> jobList;
    ArrayList<Part> partList;
    public FirebaseDatabase mFirebaseDatabase;
    public DatabaseReference mDatabaseReference;
    public DatabaseReference vehicleRef;
    public DatabaseReference workOrderRef;
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
    Button continueBtn;
    EditText notesET;
    User user;
    Vehicle vehicle;
    private PosClient posClient;
    private static final int CHARGE_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_work_order);
        posClient = PosSdk.createClient(this, "sq0idp-sfg3Kl6Gc2rZWX-q0GTnNw");
        totalPriceTV = findViewById(R.id.totalPriceTV);
        nameTV = findViewById(R.id.nameTV);
        addressTV = findViewById(R.id.addressTV);
        numberTV =findViewById(R.id.numberTV);
        vehicleNameTV = findViewById(R.id.vehicleNameTV);
        totalLaborTV = findViewById(R.id.totalLaborTV);
        totalPartsTV = findViewById(R.id.totalPartsTV);
        continueBtn = findViewById(R.id.continueBtn);
        notesET = findViewById(R.id.notesET);
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
        workOrderRef = mFirebaseDatabase.getReference("workorders/" + userID + "/" + vehicleKey);
        Log.d("User: ", userID);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    user = dataSnapshot.getValue(User.class);
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
                vehicle = dataSnapshot.getValue(Vehicle.class);
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

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int convertDouble = (int) totalPrice;
                String squareString = "" + convertDouble + "00";
                String notes = notesET.getText().toString();
                WorkOrder workOrder = new WorkOrder(user, vehicle, notes, jobList, partList, partTotal, jobTotal, totalPrice);
                workOrderRef.push().setValue(workOrder);
                int squarePrice = Integer.parseInt(squareString);
                startTransaction(squarePrice, USD);

            }
        });
    }

    public void startTransaction(int totalAmount, CurrencyCode currencyCode) {
        ChargeRequest request = new ChargeRequest.Builder(totalAmount, currencyCode).build();
        try {
            Intent intent = posClient.createChargeIntent(request);
            startActivityForResult(intent, CHARGE_REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            showDialog("Error", "Square Point of Sale is not installed", null);
            posClient.openPointOfSalePlayStoreListing();
        }
    }

    private void showDialog(String title, String message, DialogInterface.OnClickListener listener) {
        Log.d("MainActivity", title + " " + message);
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, listener)
                .show();
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHARGE_REQUEST_CODE) {
            if (data == null) {
                showDialog("Error", "Square Point of Sale was uninstalled or crashed", null);
                return;
            }

            if (resultCode == Activity.RESULT_OK) {
                ChargeRequest.Success success = posClient.parseChargeSuccess(data);
                String message = "Client transaction id: " + success.clientTransactionId;
                showDialog("Success!", message, null);

            } else {
                ChargeRequest.Error error = posClient.parseChargeError(data);

                if (error.code == ChargeRequest.ErrorCode.TRANSACTION_ALREADY_IN_PROGRESS) {
                    String title = "A transaction is already in progress";
                    String message = "Please complete the current transaction in Point of Sale.";

                    showDialog(title, message, new DialogInterface.OnClickListener() {
                        @Override public void onClick(DialogInterface dialog, int which) {
                            // Some errors can only be fixed by launching Point of Sale
                            // from the Home screen.
                            posClient.launchPointOfSale();
                        }
                    });
                } else {
                    showDialog("Error: " + error.code, error.debugDescription, null);
                }
            }
        }
    }
}
