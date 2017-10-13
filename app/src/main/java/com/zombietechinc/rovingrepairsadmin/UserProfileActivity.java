package com.zombietechinc.rovingrepairsadmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileActivity extends AppCompatActivity {

    String userID;
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mReference = mDatabase.getReference();
    DatabaseReference appointmentRef = mDatabase.getReference("appointments");
    DatabaseReference vehicleRef = mDatabase.getReference("vehicles");
    DatabaseReference userRef = mDatabase.getReference("users");
    RecyclerView mRecyclerView;
    FirebaseRecyclerAdapter mFirebaseRecyclerAdapter;
    LinearLayoutManager mLinearLayoutManager;
    TextView userNameTV;
    TextView userNumberTV;
    TextView userAddressTV;
    boolean hasAppointment = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        userNameTV = (TextView)findViewById(R.id.userName);
        userAddressTV = (TextView)findViewById(R.id.userAddress);
        userNumberTV = (TextView)findViewById(R.id.userNumber);
        mRecyclerView = (RecyclerView)findViewById(R.id.rv2);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        userRef = mDatabase.getReference("users/" + userID);
        vehicleRef = mDatabase.getReference("vehicles/" + userID);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    userNameTV.setText(user.getName() + " " + user.getLastName());
                    userNumberTV.setText(user.getContactnumber());
                    userAddressTV.setText(user.getAddress());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        userRef.addValueEventListener(valueEventListener);

        mFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Vehicle, VehicleHolder>
                (Vehicle.class, R.layout.vehicle_card, VehicleHolder.class, vehicleRef) {

            @Override
            protected void populateViewHolder(VehicleHolder holder, Vehicle vehicle, int position) {
                holder.vehicleTV.setText(vehicle.getYear() + " " + vehicle.getMake() + " " + vehicle.getModel());
                hasAppointment(vehicle);

            }
        };

        mRecyclerView.setAdapter(mFirebaseRecyclerAdapter);


    }

    private boolean hasAppointment(final Vehicle vehicle){

        appointmentRef = mDatabase.getReference("appointments/" + userID + "/" + vehicle.getUniqueKey());
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    Appointment appointment = dataSnapshot.getValue(Appointment.class);
                    Log.d("Appointment? : ", appointment.productName);
                    hasAppointment = true;
                    Log.d("appointment? ", vehicle.getUniqueKey() + " has appointment " + String.valueOf(hasAppointment));
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        appointmentRef.addChildEventListener(childEventListener);

        return hasAppointment;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.addVehicle:
                Intent intent = new Intent(UserProfileActivity.this, NewCustomerActivity.class);
                startActivity(intent);
                return true;



            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
