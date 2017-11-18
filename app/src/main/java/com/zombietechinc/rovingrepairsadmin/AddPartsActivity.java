package com.zombietechinc.rovingrepairsadmin;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class AddPartsActivity extends AppCompatActivity implements EnterPriceDialogFragment.NoticeDialogListener{

    ArrayList<Job> jobList;
    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    private LinearLayoutManager manager;
    String serviceSelected;
    Button addPartButton;
    EditText addPartET;
    ArrayList<Part> partList;
    RecyclerView partRecyclerView;
    RecyclerView.Adapter partAdapter;
    private LinearLayoutManager partManager;
    Button continueBtn;
    String userID;
    String vehicleKey;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_parts);
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        vehicleKey = intent.getStringExtra("vehicleKey");
        jobList = getIntent().getParcelableArrayListExtra("joblist");
        addPartButton = findViewById(R.id.add_part_btn);
        continueBtn = findViewById(R.id.continueBtn);
        addPartET = findViewById(R.id.partET);
        recyclerView = findViewById(R.id.second_job_rv);
        mAdapter = new JobAdapter(AddPartsActivity.this, jobList);
        manager = new LinearLayoutManager(AddPartsActivity.this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);
        partManager = new LinearLayoutManager(AddPartsActivity.this);
        partList = new ArrayList<>();
        partRecyclerView = findViewById(R.id.part_rv);
        partAdapter = new PartAdapter(AddPartsActivity.this, partList);
        partRecyclerView.setLayoutManager(partManager);
        partRecyclerView.setAdapter(partAdapter);
        addPartET.setText("");

        addPartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                serviceSelected = addPartET.getText().toString();
                showDialog();

            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddPartsActivity.this, ConfirmWorkOrderActivity.class);
                intent.putExtra("joblist", jobList);
                intent.putExtra("partlist", partList);
                intent.putExtra("userID", userID);
                intent.putExtra("vehicleKey", vehicleKey);
                startActivity(intent);
            }
        });

    }

    void showDialog() {
        // Create the fragment and show it as a dialog.
        Bundle bundle = new Bundle();
        bundle.putString("service", serviceSelected);

        DialogFragment newFragment = EnterPriceDialogFragment.newInstance();
        newFragment.setArguments(bundle);
        newFragment.show(getFragmentManager(), "dialog");


    }

    @Override
    public void onConfirmButtonClick(String name, double price) {

        Log.d("Service & Price: ", name + String.valueOf(price));
        Part part = new Part(name, price);
        partList.add(part);
        Log.d("Job Info: ", part.getName() + String.valueOf(part.getPrice()));
        addPartET.setText("");
        mAdapter.notifyDataSetChanged();

    }
}
