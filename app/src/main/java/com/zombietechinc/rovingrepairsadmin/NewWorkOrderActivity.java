package com.zombietechinc.rovingrepairsadmin;

import android.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewWorkOrderActivity extends AppCompatActivity implements EnterPriceDialogFragment.NoticeDialogListener{
    LinearLayout mLinearLayout;
    EditText laborET;
    EditText partsET;
    Button laborBtn;
    Button partsBtn;
    List <String> serviceList;
    List <String> serviceSubList;
    List <String> workOrderItems = new ArrayList<>();
    String serviceSelected;

    ArrayList<Job> jobList;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    private LinearLayoutManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_work_order);
        mRecyclerView = (RecyclerView)findViewById(R.id.jobRV);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        serviceList = Arrays.asList(getResources().getStringArray(R.array.services_list));
        ListView serviceLV = findViewById(R.id.serviceLV);
        final ListView subServiceLV = findViewById(R.id.subServiceLV);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, serviceList);
        serviceLV.setAdapter(adapter);
        jobList = new ArrayList<Job>();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                manager = new LinearLayoutManager(NewWorkOrderActivity.this);
                mRecyclerView.setLayoutManager(manager);
                mAdapter = new JobAdapter(NewWorkOrderActivity.this, jobList);

                mRecyclerView.setAdapter(mAdapter);

            }
        });


        serviceLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String service = serviceList.get(i);
                switch (service) {
                    case "Brakes": serviceSubList = Arrays.asList(getResources().getStringArray(R.array.brakes_list));
                        break;
                    case "Engine": serviceSubList = Arrays.asList(getResources().getStringArray(R.array.engine_list));
                        break;
                    case "Transmission": serviceSubList = Arrays.asList(getResources().getStringArray(R.array.transmission_list));
                        break;
                    case "Suspension": serviceSubList = Arrays.asList(getResources().getStringArray(R.array.suspension_list));
                        break;
                    case "A/C and Heating": serviceSubList = Arrays.asList(getResources().getStringArray(R.array.hvac_list));
                        break;
                    case "Electrical": serviceSubList = Arrays.asList(getResources().getStringArray(R.array.electrical_list));
                        break;
                    default: break;

                }
                ArrayAdapter arrayAdapter = new ArrayAdapter(NewWorkOrderActivity.this, android.R.layout.simple_list_item_1, serviceSubList);
                subServiceLV.setAdapter(arrayAdapter);

            }
        });


        laborET = findViewById(R.id.laborET);

        laborBtn = findViewById(R.id.addLaborbtn);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        subServiceLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                serviceSelected = serviceSubList.get(i);
                showDialog();



                workOrderItems.add(serviceSelected);
                for (int j = 0; j < workOrderItems.size(); j++) {
                    Log.d("Services added: ", workOrderItems.get(j));
                }

                Log.d("Size: ", String.valueOf(workOrderItems.size()));
                TextView textView = new TextView(NewWorkOrderActivity.this);
                textView.setText(serviceSelected);

            }
        });

        laborBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView = new TextView(NewWorkOrderActivity.this);
                mLinearLayout.addView(textView);
                textView.setText(laborET.getText());
                laborET.setText("");
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
        Job job = new Job(name, price);
        jobList.add(job);
        mAdapter.notifyDataSetChanged();

    }
}
