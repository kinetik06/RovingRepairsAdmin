package com.zombietechinc.rovingrepairsadmin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    String userEmail = "rovingrepairs@gmail.com";
    String userPassword = "m6arc6us";
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mReference = mDatabase.getReference();
    DatabaseReference appointmentRef = mDatabase.getReference("appointments");
    DatabaseReference vehicleRef = mDatabase.getReference("vehicles");
    DatabaseReference userRef = mDatabase.getReference("users");
    RecyclerView mRecyclerView;
    FirebaseRecyclerAdapter mFirebaseRecyclerAdapter;
    LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView)findViewById(R.id.rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null){
                    Log.d("User Signed in: ", firebaseUser.getDisplayName());
                    mFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<User, UserHolder>
                            (User.class, R.layout.user_card, UserHolder.class, userRef) {
                        @Override
                        protected void populateViewHolder(UserHolder holder, User user, final int position) {
                            holder.userNameTV.setText(user.getName() + " " + user.getLastName());
                            holder.userNumberTV.setText(user.getContactnumber());
                            holder.userNameTV.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    DatabaseReference ref = mFirebaseRecyclerAdapter.getRef(position);
                                    String userID = ref.getKey();
                                    Log.d("This is the user ID: ", userID);
                                    Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                                    intent.putExtra("userID", userID);
                                    startActivity(intent);
                                }
                            });

                        }
                    };

                    mRecyclerView.setAdapter(mFirebaseRecyclerAdapter);

                }else {
                    mAuth.signInWithEmailAndPassword(userEmail, userPassword);



                }
            }
        };

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
            case R.id.newCustomer:
                Intent intent = new Intent(MainActivity.this, NewCustomerActivity.class);
                startActivity(intent);
                return true;



            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthStateListener);
    }
}
