package com.zombietechinc.rovingrepairsadmin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NewCustomerActivity extends AppCompatActivity {

    EditText firstNameET;
    EditText lastNameET;
    EditText emailET;
    EditText numberET;
    EditText addressET;
    Button submitBtn;
    String customer = "customers?";
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
    OkHttpClient client2 = new OkHttpClient();
    OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    String data;
    String location;
    String location2;
    String bookeoId;
    DatabaseReference userRef = mDatabase.getReference("users");
    String customerFullURL;

    //String updateCustomerURL = getString(R.string.BOOKEO_URL) + "customers/"+ bookeoId + "?" + "/" + "&secretKey=" + getString(R.string.BOOKEO_SECRET_KEY) + "&apiKey=" + getString(R.string.BOOKEO_API_KEY_TWO);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_customer);
        customerFullURL = getString(R.string.BOOKEO_URL) + customer + "&secretKey=" + getString(R.string.BOOKEO_SECRET_KEY) + "&apiKey=" + getString(R.string.BOOKEO_API_KEY_TWO);
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
                                        String createCustomerPost = createCustomer.createCustomerPost(user);
                                        Log.d("User: ", mAuth2.getCurrentUser().getUid());
                                        userID = mAuth2.getCurrentUser().getUid();
                                        userRef = mDatabase.getReference("users/" + userID);

                                        try {

                                            String response = post(customerFullURL, createCustomerPost);
                                            Intent intent = new Intent(NewCustomerActivity.this, MainActivity.class);
                                            startActivity(intent);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }else {
                                        Log.d("Error : ", "Customer not created");
                                    }
                                }

                            });


                }
            }
        });


    }


    String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        final Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("postman-token", "27839720-3ff4-6f11-5f1b-799e826cdd75")
                .build();
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Response response = client.newCall(request).execute();
                response = client.newCall(request).execute();
                data = response.body().string();
                data = response.headers().toString();
                location = response.header("Location");
                location2 = response.header("Location", "URI");
                String other = response.request().url().toString();

                Headers headers = response.headers();
                ArrayList<Pair<String, String>> headersList = new ArrayList<>();
                for (int i = 0, j = headers.size(); i < j; i++) {
                    headersList.add(Pair.create(headers.name(i), headers.value(i)));
                }

                Log.d("Data from header", data);
                Log.d("Header stuff  :", String.valueOf(headersList));
                bookeoId = location.substring(36);
                user.setBookeoId(bookeoId);
                Log.d("Bookio ID: ", bookeoId);

                userRef.setValue(user);







                Log.d("Response Bitch!", location + " " + bookeoId);
            }
        });
        return data;
    }

    String put(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        final Request request = new Request.Builder()
                .url(url)
                .put(body)
                .addHeader("content-type", "application/json")
                .addHeader("cache-control", "no-cache")
                .build();
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Response response = client.newCall(request).execute();
                data = response.body().string();
                data = response.headers().toString();
                Log.d("Customer Updated: ", data + response.code() + response.body().toString());
                user.setBookeoId(bookeoId);
                userRef.setValue(user);
            }
        });
        return data;
    }
}
