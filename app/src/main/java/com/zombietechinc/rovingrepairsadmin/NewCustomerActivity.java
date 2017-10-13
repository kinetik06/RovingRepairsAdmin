package com.zombietechinc.rovingrepairsadmin;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewCustomerActivity extends AppCompatActivity {

    EditText firstNameET;
    EditText lastNameET;
    EditText emailET;
    EditText numberET;
    EditText addressET;
    Button submitBtn;
    private FirebaseAuth mAuth;
    private FirebaseAuth mAuth2;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    String userEmail = "";
    String userPassword = "repairs2u";
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mReference = mDatabase.getReference();
    User user;
    String bookioID;
    String userID;
    //String customerFullURL = getString(R.string.BOOKEO_URL) + customer + "&secretKey=" + getString(R.string.BOOKEO_SECRET_KEY) + "&apiKey=" + getString(R.string.BOOKEO_API_KEY_TWO);
    //String updateCustomerURL = getString(R.string.BOOKEO_URL) + "customers/"+ bookeoId + "?" + "/" + "&secretKey=" + getString(R.string.BOOKEO_SECRET_KEY) + "&apiKey=" + getString(R.string.BOOKEO_API_KEY_TWO);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_customer);

        firstNameET = findViewById(R.id.firstnameET);
        lastNameET = findViewById(R.id.lastnameET);
        emailET = findViewById(R.id.emailET);
        numberET = findViewById(R.id.numberET);
        addressET = findViewById(R.id.addressET);
        submitBtn = findViewById(R.id.submitbtn);

        FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                .setDatabaseUrl("https://roving-repairs-ac772.firebaseio.com")
                .setApiKey("AIzaSyCnBctCg7GudQW55zPxRbakdoWjcK7Bd4o")
                .setApplicationId("roving-repairs-ac772").build();

        FirebaseApp myApp = FirebaseApp.initializeApp(getApplicationContext(),firebaseOptions,
                "AnyAppName");

        mAuth2 = FirebaseAuth.getInstance(myApp);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firstNameET.getText() == null || lastNameET.getText() == null
                        || emailET.getText() == null || numberET.getText() == null || addressET.getText() == null) {
                    Toast.makeText(NewCustomerActivity.this, "Please fill out form completely!", Toast.LENGTH_SHORT).show();
                } else {

                    user = new User(firstNameET.getText().toString(), lastNameET.getText().toString(),
                            addressET.getText().toString(), numberET.getText().toString(), emailET.getText().toString() );
                    Log.d("User Info: ", user.getEmailAddress());
                    mAuth2.createUserWithEmailAndPassword(user.getEmailAddress(), userPassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        CreateCustomer createCustomer = new CreateCustomer();
                                        createCustomer.createCustomerPost(user);
                                        Log.d("User: ", mAuth2.getCurrentUser().toString());

                                    }
                                }
                            });


                }
            }
        });

    }
}
